package com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation

import com.github.hakandindis.plugins.adbhub.core.coroutine.safeLaunch
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase.*
import com.github.hakandindis.plugins.adbhub.service.RecentDeepLinksService
import com.intellij.openapi.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PackageActionsViewModel(
    private val launchAppUseCase: LaunchAppUseCase,
    private val forceStopUseCase: ForceStopUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val clearCacheUseCase: ClearCacheUseCase,
    private val uninstallUseCase: UninstallUseCase,
    private val launchDeepLinkUseCase: LaunchDeepLinkUseCase,
    private val setStayAwakeUseCase: SetStayAwakeUseCase,
    private val setPackageEnabledUseCase: SetPackageEnabledUseCase,
    private val recentDeepLinksService: RecentDeepLinksService,
    coroutineScope: CoroutineScope
) : Disposable {

    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(PackageActionsUiState())
    val uiState: StateFlow<PackageActionsUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(recentUris = recentDeepLinksService.getRecentUris()) }
    }

    fun handleIntent(intent: PackageActionsIntent) {
        when (intent) {
            is PackageActionsIntent.LaunchApp -> launchApp(intent.packageName, intent.deviceId)
            is PackageActionsIntent.ForceStop -> forceStop(intent.packageName, intent.deviceId)
            is PackageActionsIntent.ClearData -> clearData(intent.packageName, intent.deviceId)
            is PackageActionsIntent.ClearCache -> clearCache(intent.packageName, intent.deviceId)
            is PackageActionsIntent.Uninstall -> uninstall(intent.packageName, intent.deviceId)
            is PackageActionsIntent.LaunchDeepLink -> launchDeepLink(intent.uri, intent.deviceId)

            is PackageActionsIntent.StayAwake -> setStayAwake(intent.enabled, intent.deviceId)
            is PackageActionsIntent.PackageEnabled -> setPackageEnabled(
                intent.packageName,
                intent.enabled,
                intent.deviceId
            )
        }
    }

    private fun launchApp(packageName: String, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isLaunching = true, error = null) }
            launchAppUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLaunching = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLaunching = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    private fun forceStop(packageName: String, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isStopping = true, error = null) }
            forceStopUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isStopping = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isStopping = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    private fun clearData(packageName: String, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isClearingData = true, error = null) }
            clearDataUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isClearingData = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isClearingData = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    private fun clearCache(packageName: String, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isClearingCache = true, error = null) }
            clearCacheUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isClearingCache = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isClearingCache = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    private fun uninstall(packageName: String, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isUninstalling = true, error = null) }
            uninstallUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isUninstalling = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isUninstalling = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    private fun launchDeepLink(uri: String, deviceId: String) {
        scope.safeLaunch {
            launchDeepLinkUseCase(uri, deviceId).fold(
                onSuccess = {
                    recentDeepLinksService.addAndTruncate(uri)
                    _uiState.update { it.copy(recentUris = recentDeepLinksService.getRecentUris()) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(error = error.toUserMessage())
                    }
                }
            )
        }
    }

    private fun setStayAwake(enabled: Boolean, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isSettingStayAwake = true, error = null) }
            setStayAwakeUseCase(enabled, deviceId).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSettingStayAwake = false,
                            stayAwakeEnabled = enabled
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSettingStayAwake = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }

    private fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String) {
        scope.safeLaunch {
            _uiState.update { it.copy(isSettingEnabled = true, error = null) }
            setPackageEnabledUseCase(packageName, enabled, deviceId).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSettingEnabled = false,
                            packageEnabled = enabled
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSettingEnabled = false,
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
