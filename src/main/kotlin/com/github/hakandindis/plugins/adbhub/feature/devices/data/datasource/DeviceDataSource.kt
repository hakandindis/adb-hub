package com.github.hakandindis.plugins.adbhub.feature.devices.data.datasource

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

/**
 * Data source interface for device-related operations
 */
interface DeviceDataSource {
    /**
     * Gets list of connected devices
     */
    suspend fun getDevices(): List<Device>

    /**
     * Gets detailed information about a device
     * @param deviceId Device ID (serial number)
     * @return DeviceInfo with detailed device information, or null if not available
     */
    suspend fun getDeviceInfo(deviceId: String): DeviceInfo?
}
