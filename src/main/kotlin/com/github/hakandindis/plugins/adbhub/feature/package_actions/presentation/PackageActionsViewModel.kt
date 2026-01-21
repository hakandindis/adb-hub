package com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase.*
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
 * ViewModel for Package Actions feature (MVI pattern)
 */
class PackageActionsViewModel(
    private val launchAppUseCase: LaunchAppUseCase,
    private val forceStopUseCase: ForceStopUseCase,
    private val restartAppUseCase: RestartAppUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val clearCacheUseCase: ClearCacheUseCase,
    private val uninstallUseCase: UninstallUseCase,
    private val launchDeepLinkUseCase: LaunchDeepLinkUseCase,
    private val setStayAwakeUseCase: SetStayAwakeUseCase,
    private val setPackageEnabledUseCase: SetPackageEnabledUseCase,
    coroutineScope: CoroutineScope
) : Disposable {

    private val logger = Logger.getInstance(PackageActionsViewModel::class.java)
    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(PackageActionsUiState())
    val uiState: StateFlow<PackageActionsUiState> = _uiState.asStateFlow()

    /**
     * Handles intents from UI
     */
    fun handleIntent(intent: PackageActionsIntent) {
        when (intent) {
            is PackageActionsIntent.LaunchApp -> launchApp(intent.packageName, intent.deviceId)
            is PackageActionsIntent.LaunchActivity -> launchActivity(intent.activityName, intent.deviceId)
            is PackageActionsIntent.ForceStop -> forceStop(intent.packageName, intent.deviceId)
            is PackageActionsIntent.RestartApp -> restartApp(intent.packageName, intent.deviceId)
            is PackageActionsIntent.ClearData -> clearData(intent.packageName, intent.deviceId)
            is PackageActionsIntent.ClearCache -> clearCache(intent.packageName, intent.deviceId)
            is PackageActionsIntent.Uninstall -> uninstall(intent.packageName, intent.deviceId)
            is PackageActionsIntent.LaunchDeepLink -> launchDeepLink(intent.deepLink, intent.deviceId)
            is PackageActionsIntent.SetStayAwake -> setStayAwake(intent.enabled, intent.deviceId)
            is PackageActionsIntent.SetPackageEnabled -> setPackageEnabled(
                intent.packageName,
                intent.enabled,
                intent.deviceId
            )
        }
    }

    private fun launchApp(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isLaunching = true, error = null) }
            launchAppUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLaunching = false) }
                },
                onFailure = { error ->
                    logger.error("Error launching app $packageName", error)
                    _uiState.update {
                        it.copy(
                            isLaunching = false,
                            error = error.message ?: "Failed to launch app"
                        )
                    }
                }
            )
        }
    }

    private fun launchActivity(activityName: String, deviceId: String) {
        scope.launch {
            // Activity launch is handled by PackageDetailsViewModel
            // This is kept for consistency but may not be used
        }
    }

    private fun forceStop(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isStopping = true, error = null) }
            forceStopUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isStopping = false) }
                },
                onFailure = { error ->
                    logger.error("Error force stopping app $packageName", error)
                    _uiState.update {
                        it.copy(
                            isStopping = false,
                            error = error.message ?: "Failed to force stop app"
                        )
                    }
                }
            )
        }
    }

    private fun restartApp(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isRestarting = true, error = null) }
            restartAppUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isRestarting = false) }
                },
                onFailure = { error ->
                    logger.error("Error restarting app $packageName", error)
                    _uiState.update {
                        it.copy(
                            isRestarting = false,
                            error = error.message ?: "Failed to restart app"
                        )
                    }
                }
            )
        }
    }

    private fun clearData(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isClearingData = true, error = null) }
            clearDataUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isClearingData = false) }
                },
                onFailure = { error ->
                    logger.error("Error clearing data for $packageName", error)
                    _uiState.update {
                        it.copy(
                            isClearingData = false,
                            error = error.message ?: "Failed to clear data"
                        )
                    }
                }
            )
        }
    }

    private fun clearCache(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isClearingCache = true, error = null) }
            clearCacheUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isClearingCache = false) }
                },
                onFailure = { error ->
                    logger.error("Error clearing cache for $packageName", error)
                    _uiState.update {
                        it.copy(
                            isClearingCache = false,
                            error = error.message ?: "Failed to clear cache"
                        )
                    }
                }
            )
        }
    }

    private fun uninstall(packageName: String, deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isUninstalling = true, error = null) }
            uninstallUseCase(packageName, deviceId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isUninstalling = false) }
                },
                onFailure = { error ->
                    logger.error("Error uninstalling app $packageName", error)
                    _uiState.update {
                        it.copy(
                            isUninstalling = false,
                            error = error.message ?: "Failed to uninstall app"
                        )
                    }
                }
            )
        }
    }

    private fun launchDeepLink(deepLink: String, deviceId: String) {
        scope.launch {
            launchDeepLinkUseCase(deepLink, deviceId).fold(
                onSuccess = {
                    // Success - no state update needed
                },
                onFailure = { error ->
                    logger.error("Error launching deep link $deepLink", error)
                    _uiState.update {
                        it.copy(error = error.message ?: "Failed to launch deep link")
                    }
                }
            )
        }
    }

    private fun setStayAwake(enabled: Boolean, deviceId: String) {
        scope.launch {
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
                    logger.error("Error setting stay awake", error)
                    _uiState.update {
                        it.copy(
                            isSettingStayAwake = false,
                            error = error.message ?: "Failed to set stay awake"
                        )
                    }
                }
            )
        }
    }

    private fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String) {
        scope.launch {
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
                    logger.error("Error ${if (enabled) "enabling" else "disabling"} package $packageName", error)
                    _uiState.update {
                        it.copy(
                            isSettingEnabled = false,
                            error = error.message ?: "Failed to ${if (enabled) "enable" else "disable"} package"
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
