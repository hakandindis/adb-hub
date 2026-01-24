package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.constants.AmCommands
import com.github.hakandindis.plugins.adbhub.constants.DumpsysCommands
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.ActivityMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.GeneralInfoMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.PermissionMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetPackageDetailsUseCase
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ActivityUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionUiModel
import com.github.hakandindis.plugins.adbhub.models.PermissionGrantStatus
import com.github.hakandindis.plugins.adbhub.models.PermissionStatus
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
        }
    }

    private fun updatePermissionSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                permissionSearchText = query,
                filteredPermissions = filterPermissions(state.permissions, query)
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

    private fun filterPermissions(permissions: List<PermissionUiModel>, query: String): List<PermissionUiModel> {
        return if (query.isBlank()) permissions
        else permissions.filter { it.name.contains(query, ignoreCase = true) }
    }

    private fun filterActivities(activities: List<ActivityUiModel>, query: String): List<ActivityUiModel> {
        return if (query.isBlank()) activities
        else activities.filter {
            it.name.contains(query, ignoreCase = true) || it.shortName.contains(
                query,
                ignoreCase = true
            )
        }
    }


    private fun loadPackageDetails(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getPackageDetailsUseCase(packageName, deviceId).fold(
                onSuccess = { packageDetails ->
                    val generalInfoItems = GeneralInfoMapper.toMergedInfoItems(packageDetails)
                    val activities = packageDetails.activities.map { activity ->
                        ActivityMapper.toUiModel(activity)
                    }

                    val permissionStatuses = loadPermissionStatuses(packageDetails.permissions, packageName, deviceId)
                    val permissionUiModels = PermissionMapper.toUiModels(permissionStatuses)

                    _uiState.update {
                        it.copy(
                            generalInfoItems = generalInfoItems,
                            activities = activities,
                            permissions = permissionUiModels,
                            permissionSearchText = "",
                            filteredPermissions = permissionUiModels,
                            activitySearchText = "",
                            filteredActivities = activities,
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

    /**
     * Loads permission statuses from dumpsys output
     * TODO: Move this to a separate use case and parser
     */
    private suspend fun loadPermissionStatuses(
        permissions: List<String>,
        packageName: String,
        deviceId: String
    ): List<PermissionStatus> {
        if (commandExecutor == null || permissions.isEmpty()) {
            return emptyList()
        }

        return try {
            val result =
                commandExecutor.executeCommandForDevice(deviceId, DumpsysCommands.getPackageDumpsys(packageName))
            if (result.isSuccess) {
                permissions.map { permission ->
                    val status = determinePermissionStatus(permission, result.output)
                    PermissionStatus(
                        permission = permission,
                        status = status
                    )
                }
            } else {
                // Fallback: mark all as optional
                permissions.map {
                    PermissionStatus(
                        permission = it,
                        status = PermissionGrantStatus.OPTIONAL
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("Error loading permission statuses for $packageName", e)
            emptyList()
        }
    }

    /**
     * Determines permission status from dumpsys output
     * TODO: Move this to PermissionsParser
     */
    private fun determinePermissionStatus(
        permission: String,
        dumpsysOutput: String
    ): PermissionGrantStatus {
        val grantedPattern = (ParsePatterns.GRANTED_PATTERN.pattern + permission).toRegex(RegexOption.IGNORE_CASE)
        if (grantedPattern.find(dumpsysOutput) != null) {
            return PermissionGrantStatus.GRANTED
        }

        val deniedPattern = (ParsePatterns.DENIED_PATTERN.pattern + permission).toRegex(RegexOption.IGNORE_CASE)
        if (deniedPattern.find(dumpsysOutput) != null) {
            return PermissionGrantStatus.DENIED
        }

        return PermissionGrantStatus.OPTIONAL
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
