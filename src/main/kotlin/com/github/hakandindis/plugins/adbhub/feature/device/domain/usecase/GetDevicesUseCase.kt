package com.github.hakandindis.plugins.adbhub.feature.device.domain.usecase

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.device.domain.repository.DeviceRepository

/**
 * Use case for getting list of connected devices
 */
class GetDevicesUseCase(
    private val repository: DeviceRepository
) {
    /**
     * Executes the use case
     * @return Result containing list of devices or error
     */
    suspend operator fun invoke(): Result<List<Device>> {
        return repository.getDevices()
    }
}
