package com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

interface DeviceRepository {
    suspend fun getDevices(): Result<List<Device>>
    suspend fun getDeviceInfo(deviceId: String): Result<DeviceInfo>
}
