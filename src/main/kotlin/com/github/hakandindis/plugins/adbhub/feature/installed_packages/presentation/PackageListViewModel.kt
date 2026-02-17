package com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation

import com.github.hakandindis.plugins.adbhub.core.coroutine.safeLaunch
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.core.selection.SelectionManager
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase.FilterPackagesUseCase
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase.GetPackagesUseCase
import com.intellij.openapi.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

class PackageListViewModel(
    private val getPackagesUseCase: GetPackagesUseCase,
    private val filterPackagesUseCase: FilterPackagesUseCase,
    private val selectionManager: SelectionManager,
    coroutineScope: CoroutineScope
) : Disposable {

    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(PackageListUiState())
    val uiState: StateFlow<PackageListUiState> = _uiState.asStateFlow()

    init {
        scope.safeLaunch {
            merge(
                selectionManager.selectedDeviceState,
                selectionManager.deviceRefreshRequest.map { selectionManager.selectedDeviceState.value }
            ).collectLatest { device ->
                device?.let {
                    if (it.state == DeviceState.DEVICE) {
                        refreshPackages(it.id, includeSystemApps = true)
                    }
                }
            }
        }
    }

    fun handleIntent(intent: PackageListIntent) {
        when (intent) {
            is PackageListIntent.SearchPackages -> updateSearchText(intent.query)
            is PackageListIntent.SelectPackage -> selectionManager.selectPackage(intent.packageItem)
            is PackageListIntent.RefreshPackages -> refreshPackages(intent.deviceId, intent.includeSystemApps)
        }
    }

    private fun updateSearchText(text: String) {
        _uiState.update { currentState ->
            val filtered = filterPackagesUseCase(
                packages = currentState.packages,
                searchText = text,
            )
            currentState.copy(
                searchText = text,
                filteredPackages = filtered
            )
        }
    }

    private fun refreshPackages(deviceId: String, includeSystemApps: Boolean) {
        scope.safeLaunch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPackagesUseCase(deviceId, includeSystemApps).fold(
                onSuccess = { packages ->
                    val filtered = filterPackagesUseCase(
                        packages = packages,
                        searchText = _uiState.value.searchText,
                    )
                    _uiState.update {
                        it.copy(
                            packages = packages,
                            filteredPackages = filtered,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    override fun dispose() {
        scope.cancel()
    }
}
