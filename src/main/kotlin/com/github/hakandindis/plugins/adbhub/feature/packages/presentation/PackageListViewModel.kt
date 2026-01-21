package com.github.hakandindis.plugins.adbhub.feature.packages.presentation

import com.github.hakandindis.plugins.adbhub.feature.packages.domain.usecase.FilterPackagesUseCase
import com.github.hakandindis.plugins.adbhub.feature.packages.domain.usecase.GetPackagesUseCase
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
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
 * ViewModel for Package List feature (MVI pattern)
 */
class PackageListViewModel(
    private val getPackagesUseCase: GetPackagesUseCase,
    private val filterPackagesUseCase: FilterPackagesUseCase,
    coroutineScope: CoroutineScope
) : Disposable {

    private val logger = Logger.getInstance(PackageListViewModel::class.java)
    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(PackageListUiState())
    val uiState: StateFlow<PackageListUiState> = _uiState.asStateFlow()

    /**
     * Handles intents from UI
     */
    fun handleIntent(intent: PackageListIntent) {
        when (intent) {
            is PackageListIntent.SetSearchText -> updateSearchText(intent.text)
            is PackageListIntent.SetShowSystemApps -> updateShowSystemApps(intent.show)
            is PackageListIntent.SetShowUserApps -> updateShowUserApps(intent.show)
            is PackageListIntent.SetShowDebugApps -> updateShowDebugApps(intent.show)
            is PackageListIntent.SelectPackage -> selectPackage(intent.packageItem)
            is PackageListIntent.RefreshPackages -> refreshPackages(intent.deviceId, intent.includeSystemApps)
        }
    }

    /**
     * Updates search text and applies filtering
     */
    private fun updateSearchText(text: String) {
        _uiState.update { currentState ->
            val filtered = filterPackagesUseCase(
                packages = currentState.packages,
                searchText = text,
                showSystemApps = currentState.showSystemApps,
                showUserApps = currentState.showUserApps,
                showDebugApps = currentState.showDebugApps
            )
            currentState.copy(
                searchText = text,
                filteredPackages = filtered
            )
        }
    }

    /**
     * Updates show system apps filter and applies filtering
     */
    private fun updateShowSystemApps(show: Boolean) {
        _uiState.update { currentState ->
            val filtered = filterPackagesUseCase(
                packages = currentState.packages,
                searchText = currentState.searchText,
                showSystemApps = show,
                showUserApps = currentState.showUserApps,
                showDebugApps = currentState.showDebugApps
            )
            currentState.copy(
                showSystemApps = show,
                filteredPackages = filtered
            )
        }
    }

    /**
     * Updates show user apps filter and applies filtering
     */
    private fun updateShowUserApps(show: Boolean) {
        _uiState.update { currentState ->
            val filtered = filterPackagesUseCase(
                packages = currentState.packages,
                searchText = currentState.searchText,
                showSystemApps = currentState.showSystemApps,
                showUserApps = show,
                showDebugApps = currentState.showDebugApps
            )
            currentState.copy(
                showUserApps = show,
                filteredPackages = filtered
            )
        }
    }

    /**
     * Updates show debug apps filter and applies filtering
     */
    private fun updateShowDebugApps(show: Boolean) {
        _uiState.update { currentState ->
            val filtered = filterPackagesUseCase(
                packages = currentState.packages,
                searchText = currentState.searchText,
                showSystemApps = currentState.showSystemApps,
                showUserApps = currentState.showUserApps,
                showDebugApps = show
            )
            currentState.copy(
                showDebugApps = show,
                filteredPackages = filtered
            )
        }
    }

    /**
     * Selects a package
     */
    private fun selectPackage(packageItem: ApplicationPackage) {
        _uiState.update { it.copy(selectedPackage = packageItem) }
    }

    /**
     * Refreshes the list of packages
     */
    private fun refreshPackages(deviceId: String, includeSystemApps: Boolean) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPackagesUseCase(deviceId, includeSystemApps).fold(
                onSuccess = { packages ->
                    val filtered = filterPackagesUseCase(
                        packages = packages,
                        searchText = _uiState.value.searchText,
                        showSystemApps = _uiState.value.showSystemApps,
                        showUserApps = _uiState.value.showUserApps,
                        showDebugApps = _uiState.value.showDebugApps
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
                    logger.error("Error refreshing packages for device $deviceId", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to refresh packages"
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
