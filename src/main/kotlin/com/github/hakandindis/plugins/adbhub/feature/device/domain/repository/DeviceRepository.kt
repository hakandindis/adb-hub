package com.github.hakandindis.plugins.adbhub.feature.device.domain.repository

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

/**
 * Repository interface for device operations
 */
interface DeviceRepository {
    /**
     * Gets list of connected devices
     * @return Result containing list of devices or error
     */
    suspend fun getDevices(): Result<List<Device>>

    /**
     * Gets detailed information about a device
     * @param deviceId Device ID (serial number)
     * @return Result containing DeviceInfo or error
     */
    suspend fun getDeviceInfo(deviceId: String): Result<DeviceInfo>
}
