package com.github.hakandindis.plugins.adbhub.feature.device.presentation

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

/**
 * UI State for Device feature
 */
data class DeviceUiState(
    val devices: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val deviceInfo: DeviceInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
