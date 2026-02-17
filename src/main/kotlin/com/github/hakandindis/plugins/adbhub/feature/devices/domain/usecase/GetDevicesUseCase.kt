package com.github.hakandindis.plugins.adbhub.feature.devices.domain.usecase

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository.DeviceRepository

class GetDevicesUseCase(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(): AdbHubResult<List<Device>> = repository.getDevices()
}
