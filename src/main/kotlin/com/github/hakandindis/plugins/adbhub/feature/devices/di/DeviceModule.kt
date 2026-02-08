package com.github.hakandindis.plugins.adbhub.feature.devices.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.devices.data.repository.DeviceRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.usecase.GetDeviceInfoUseCase
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.usecase.GetDevicesUseCase

object DeviceModule {

    fun createDeviceRepository(executor: AdbCommandExecutor): DeviceRepository {
        return DeviceRepositoryImpl(executor)
    }

    fun createGetDevicesUseCase(repository: DeviceRepository): GetDevicesUseCase {
        return GetDevicesUseCase(repository)
    }

    fun createGetDeviceInfoUseCase(repository: DeviceRepository): GetDeviceInfoUseCase {
        return GetDeviceInfoUseCase(repository)
    }
}
