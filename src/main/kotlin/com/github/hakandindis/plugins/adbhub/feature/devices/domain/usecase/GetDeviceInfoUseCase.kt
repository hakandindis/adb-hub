package com.github.hakandindis.plugins.adbhub.feature.devices.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

class GetDeviceInfoUseCase(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String): Result<DeviceInfo> {
        return repository.getDeviceInfo(deviceId)
    }
}
