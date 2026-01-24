package com.github.hakandindis.plugins.adbhub.feature.devices.presentation

import com.github.hakandindis.plugins.adbhub.core.models.Device

/**
 * Intent actions for Device feature (MVI pattern)
 */
sealed class DeviceIntent {
    /**
     * Refresh the list of connected devices
     */
    object RefreshDevices : DeviceIntent()

    /**
     * Select a device
     */
    data class SelectDevice(val device: Device) : DeviceIntent()

    /**
     * Load detailed information about a device
     */
    data class LoadDeviceInfo(val deviceId: String) : DeviceIntent()
}
