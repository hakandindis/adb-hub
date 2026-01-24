package com.github.hakandindis.plugins.adbhub.feature.devices.presentation

import com.github.hakandindis.plugins.adbhub.core.models.Device

/**
 * Intent actions for Device feature (MVI pattern)
 */
sealed class DeviceIntent {
    object RefreshDevices : DeviceIntent()
    data class SelectDevice(val device: Device) : DeviceIntent()
    data class LoadDeviceInfo(val deviceId: String) : DeviceIntent()
}
