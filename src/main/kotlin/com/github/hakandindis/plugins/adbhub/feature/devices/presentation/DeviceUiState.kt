package com.github.hakandindis.plugins.adbhub.feature.devices.presentation

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.ui.DeviceInfoItemUiModel

data class DeviceUiState(
    val devices: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val deviceInfoItems: List<DeviceInfoItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
