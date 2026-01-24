package com.github.hakandindis.plugins.adbhub.feature.devices.data.datasource

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

/**
 * Data source interface for device-related operations
 */
interface DeviceDataSource {
    suspend fun getDevices(): List<Device>
    suspend fun getDeviceInfo(deviceId: String): DeviceInfo?
}
