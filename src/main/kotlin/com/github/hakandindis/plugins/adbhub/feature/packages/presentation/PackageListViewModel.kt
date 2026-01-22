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

    fun handleIntent(intent: PackageListIntent) {
        when (intent) {
            is PackageListIntent.SetSearchText -> updateSearchText(intent.text)
            is PackageListIntent.SelectPackage -> selectPackage(intent.packageItem)
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

    private fun selectPackage(packageItem: ApplicationPackage) {
        _uiState.update { it.copy(selectedPackage = packageItem) }
    }

    private fun refreshPackages(deviceId: String, includeSystemApps: Boolean) {
        scope.launch {
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
