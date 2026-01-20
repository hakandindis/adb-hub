package com.github.hakandindis.plugins.adbhub.feature.device.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.device.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

/**
 * Use case for getting detailed information about a device
 */
class GetDeviceInfoUseCase(
    private val repository: DeviceRepository
) {
    /**
     * Executes the use case
     * @param deviceId Device ID (serial number)
     * @return Result containing DeviceInfo or error
     */
    suspend operator fun invoke(deviceId: String): Result<DeviceInfo> {
        return repository.getDeviceInfo(deviceId)
    }
}
