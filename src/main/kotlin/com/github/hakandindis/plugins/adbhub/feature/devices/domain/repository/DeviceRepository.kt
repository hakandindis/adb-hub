package com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

interface DeviceRepository {
    suspend fun getDevices(): AdbHubResult<List<Device>>
    suspend fun getDeviceInfo(deviceId: String): AdbHubResult<DeviceInfo>
}
