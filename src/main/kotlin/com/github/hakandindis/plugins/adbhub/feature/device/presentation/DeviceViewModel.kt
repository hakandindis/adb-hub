package com.github.hakandindis.plugins.adbhub.feature.device.presentation

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.feature.device.domain.usecase.GetDeviceInfoUseCase
import com.github.hakandindis.plugins.adbhub.feature.device.domain.usecase.GetDevicesUseCase
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
 * ViewModel for Device feature (MVI pattern)
 */
class DeviceViewModel(
    private val getDevicesUseCase: GetDevicesUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase,
    coroutineScope: CoroutineScope
) : Disposable {

    private val logger = Logger.getInstance(DeviceViewModel::class.java)
    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(DeviceUiState())
    val uiState: StateFlow<DeviceUiState> = _uiState.asStateFlow()

    /**
     * Handles intents from UI
     */
    fun handleIntent(intent: DeviceIntent) {
        when (intent) {
            is DeviceIntent.RefreshDevices -> refreshDevices()
            is DeviceIntent.SelectDevice -> selectDevice(intent.device)
            is DeviceIntent.LoadDeviceInfo -> loadDeviceInfo(intent.deviceId)
        }
    }

    /**
     * Refreshes the list of connected devices
     */
    private fun refreshDevices() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getDevicesUseCase().fold(
                onSuccess = { devices ->
                    _uiState.update {
                        it.copy(
                            devices = devices,
                            isLoading = false,
                            // Auto-select first available device if none selected
                            selectedDevice = it.selectedDevice ?: devices.firstOrNull { device ->
                                device.state == DeviceState.DEVICE
                            }
                        )
                    }
                },
                onFailure = { error ->
                    logger.error("Error refreshing devices", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to refresh devices"
                        )
                    }
                }
            )
        }
    }

    /**
     * Selects a device and loads its information
     */
    private fun selectDevice(device: Device) {
        _uiState.update { it.copy(selectedDevice = device) }
        if (device.state == DeviceState.DEVICE) {
            loadDeviceInfo(device.id)
        } else {
            _uiState.update { it.copy(deviceInfo = null) }
        }
    }

    /**
     * Loads detailed information about a device
     */
    private fun loadDeviceInfo(deviceId: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getDeviceInfoUseCase(deviceId).fold(
                onSuccess = { info ->
                    _uiState.update {
                        it.copy(
                            deviceInfo = info,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    logger.error("Error loading device info for $deviceId", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load device info"
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
