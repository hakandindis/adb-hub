package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.core.selection.SelectionManager
import com.github.hakandindis.plugins.adbhub.core.selection.SelectionState
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetPackageDetailsUseCase
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PackageDetailsViewModel(
    private val getPackageDetailsUseCase: GetPackageDetailsUseCase,
    private val selectionManager: SelectionManager,
    coroutineScope: CoroutineScope
) : Disposable {

    private val logger = Logger.getInstance(PackageDetailsViewModel::class.java)
    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(PackageDetailsUiState())
    val uiState: StateFlow<PackageDetailsUiState> = _uiState.asStateFlow()

    init {
        scope.launch {
            combine(
                selectionManager.selectedDeviceState,
                selectionManager.selectedPackageState
            ) { device, packageItem -> SelectionState(device, packageItem) }
                .collectLatest { state ->
                    if (state.hasValidSelection) {
                        handleIntent(
                            PackageDetailsIntent.LoadPackageDetails(
                                state.selectedPackage!!.packageName,
                                state.selectedDevice!!.id
                            )
                        )
                    } else {
                        handleIntent(PackageDetailsIntent.ClearDetails)
                    }
                }
        }
    }

    fun handleIntent(intent: PackageDetailsIntent) {
        when (intent) {
            is PackageDetailsIntent.LoadPackageDetails -> loadPackageDetails(intent.packageName, intent.deviceId)
            is PackageDetailsIntent.ClearDetails -> _uiState.value = PackageDetailsUiState()
            is PackageDetailsIntent.FilterPermissions -> updatePermissionSearch(intent.query)
            is PackageDetailsIntent.FilterActivities -> updateActivitySearch(intent.query)
            is PackageDetailsIntent.FilterReceivers -> updateReceiverSearch(intent.query)
            is PackageDetailsIntent.FilterServices -> updateServiceSearch(intent.query)
            is PackageDetailsIntent.FilterContentProviders -> updateContentProviderSearch(intent.query)
        }
    }

    private fun loadPackageDetails(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getPackageDetailsUseCase(packageName, deviceId).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            packageName = result.packageName,
                            appName = result.appName,
                            generalInfoItems = result.generalInfoItems,
                            activities = result.activities,
                            activitySearchText = "",
                            filteredActivities = result.activities,
                            services = result.services,
                            serviceSearchText = "",
                            filteredServices = result.services,
                            receivers = result.receivers,
                            receiverSearchText = "",
                            filteredReceivers = result.receivers,
                            contentProviders = result.contentProviders,
                            contentProviderSearchText = "",
                            filteredContentProviders = result.contentProviders,
                            permissionSections = result.permissionSections,
                            permissionSearchText = "",
                            filteredPermissionSections = result.permissionSections,
                            appLinks = result.appLinks,
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

    private fun updatePermissionSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                permissionSearchText = query,
                filteredPermissionSections = filterPermissionSections(state.permissionSections, query)
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
                        it.detailTextForFilter.contains(query, ignoreCase = true)
            }
            section.copy(items = filteredItems)
        }.filter { it.items.isNotEmpty() }
    }

    private fun updateActivitySearch(query: String) {
        _uiState.update { state ->
            state.copy(
                activitySearchText = query,
                filteredActivities = filterComponents(state.activities, query)
            )
        }
    }

    private fun updateReceiverSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                receiverSearchText = query,
                filteredReceivers = filterComponents(state.receivers, query)
            )
        }
    }

    private fun updateServiceSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                serviceSearchText = query,
                filteredServices = filterComponents(state.services, query)
            )
        }
    }

    private fun updateContentProviderSearch(query: String) {
        _uiState.update { state ->
            state.copy(
                contentProviderSearchText = query,
                filteredContentProviders = filterComponents(state.contentProviders, query)
            )
        }
    }

    private fun filterComponents(list: List<ComponentDisplay>, query: String): List<ComponentDisplay> {
        return if (query.isBlank()) list
        else list.filter {
            it.name.contains(query, ignoreCase = true) || it.shortName.contains(query, ignoreCase = true)
        }
    }

    override fun dispose() {
        scope.cancel()
    }
}
