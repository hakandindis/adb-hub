package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.constants.AmCommands
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.*
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetPackageDetailsUseCase
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.*
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Package Details feature (MVI pattern)
 */
class PackageDetailsViewModel(
    private val getPackageDetailsUseCase: GetPackageDetailsUseCase,
    private val commandExecutor: AdbCommandExecutor?,
    coroutineScope: CoroutineScope
) : Disposable {

    private val logger = Logger.getInstance(PackageDetailsViewModel::class.java)
    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(PackageDetailsUiState())
    val uiState: StateFlow<PackageDetailsUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: PackageDetailsIntent) {
        when (intent) {
            is PackageDetailsIntent.LoadPackageDetails -> loadPackageDetails(intent.packageName, intent.deviceId)
            is PackageDetailsIntent.LaunchActivity -> launchActivity(intent.activityName, intent.deviceId)
            is PackageDetailsIntent.FilterPermissions -> updatePermissionSearch(intent.query)
            is PackageDetailsIntent.FilterActivities -> updateActivitySearch(intent.query)
            is PackageDetailsIntent.FilterReceivers -> updateReceiverSearch(intent.query)
            is PackageDetailsIntent.FilterServices -> updateServiceSearch(intent.query)
            is PackageDetailsIntent.FilterContentProviders -> updateContentProviderSearch(intent.query)
        }
    }

    private fun updatePermissionSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                permissionSearchText = query,
                filteredPermissionSections = filterPermissionSections(state.permissionSections, query)
            )
        }
    }

    private fun updateActivitySearch(query: String) {
        _uiState.update { state ->
            state.copy(
                activitySearchText = query,
                filteredActivities = filterActivities(state.activities, query)
            )
        }
    }

    private fun filterPermissionSections(
        sections: List<PermissionSectionUiModel>,
        query: String
    ): List<PermissionSectionUiModel> {
        if (query.isBlank()) return sections
        return sections.map { section ->
            val filteredItems = section.items.filter {
                it.name.contains(query, ignoreCase = true) ||
                        (it.detail?.contains(query, ignoreCase = true) == true)
            }
            section.copy(items = filteredItems)
        }.filter { it.items.isNotEmpty() }
    }

    private fun filterActivities(activities: List<ActivityUiModel>, query: String): List<ActivityUiModel> {
        return if (query.isBlank()) activities
        else activities.filter {
            it.name.contains(query, ignoreCase = true) || it.shortName.contains(query, ignoreCase = true)
        }
    }

    private fun updateReceiverSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                receiverSearchText = query,
                filteredReceivers = filterReceiverUiModels(state.receivers, query)
            )
        }
    }

    private fun updateServiceSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                serviceSearchText = query,
                filteredServices = filterServiceUiModels(state.services, query)
            )
        }
    }

    private fun updateContentProviderSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                contentProviderSearchText = query,
                filteredContentProviders = filterContentProviderUiModels(state.contentProviders, query)
            )
        }
    }

    private fun filterReceiverUiModels(list: List<ReceiverUiModel>, query: String): List<ReceiverUiModel> {
        return if (query.isBlank()) list
        else list.filter {
            it.name.contains(query, ignoreCase = true) || it.shortName.contains(query, ignoreCase = true)
        }
    }

    private fun filterServiceUiModels(list: List<ServiceUiModel>, query: String): List<ServiceUiModel> {
        return if (query.isBlank()) list
        else list.filter {
            it.name.contains(query, ignoreCase = true) || it.shortName.contains(query, ignoreCase = true)
        }
    }

    private fun filterContentProviderUiModels(
        list: List<ContentProviderUiModel>,
        query: String
    ): List<ContentProviderUiModel> {
        return if (query.isBlank()) list
        else list.filter {
            it.name.contains(query, ignoreCase = true) || it.shortName.contains(query, ignoreCase = true)
        }
    }

    private fun loadPackageDetails(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getPackageDetailsUseCase(packageName, deviceId).fold(
                onSuccess = { packageDetails ->
                    val generalInfoItems = GeneralInfoMapper.toMergedInfoItems(packageDetails)
                    val activities = packageDetails.activities.map { ActivityMapper.toUiModel(it) }
                    val permissionSections = PermissionMapper.toUiModels(packageDetails.permissionSections)
                    val services = ServiceMapper.toUiModels(packageDetails.services)
                    val receivers = ReceiverMapper.toUiModels(packageDetails.receivers)
                    val contentProviders = ContentProviderMapper.toUiModels(packageDetails.contentProviders)

                    _uiState.update {
                        it.copy(
                            generalInfoItems = generalInfoItems,
                            activities = activities,
                            activitySearchText = "",
                            filteredActivities = activities,
                            services = services,
                            serviceSearchText = "",
                            filteredServices = services,
                            receivers = receivers,
                            receiverSearchText = "",
                            filteredReceivers = receivers,
                            contentProviders = contentProviders,
                            contentProviderSearchText = "",
                            filteredContentProviders = contentProviders,
                            permissionSections = permissionSections,
                            permissionSearchText = "",
                            filteredPermissionSections = permissionSections,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    logger.error("Error loading package details for $packageName", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load package details"
                        )
                    }
                }
            )
        }
    }

    private fun launchActivity(activityName: String, deviceId: String) {
        if (commandExecutor == null) {
            logger.warn("Cannot launch activity: ADB executor not available")
            return
        }

        scope.launch {
            try {
                val command = AmCommands.startActivity(activityName)
                val result = commandExecutor.executeCommandForDevice(deviceId, command)
                if (!result.isSuccess) {
                    logger.error("Failed to launch activity $activityName: ${result.error}")
                }
            } catch (e: Exception) {
                logger.error("Error launching activity $activityName", e)
            }
        }
    }

    override fun dispose() {
        scope.cancel()
    }
}
