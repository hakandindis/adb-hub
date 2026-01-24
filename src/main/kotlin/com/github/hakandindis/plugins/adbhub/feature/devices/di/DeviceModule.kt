package com.github.hakandindis.plugins.adbhub.feature.devices.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.devices.data.datasource.DeviceDataSource
import com.github.hakandindis.plugins.adbhub.feature.devices.data.datasource.DeviceDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.devices.data.repository.DeviceRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.usecase.GetDeviceInfoUseCase
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.usecase.GetDevicesUseCase

/**
 * Dependency injection module for Device feature
 */
object DeviceModule {

    fun createDeviceDataSource(executor: AdbCommandExecutor): DeviceDataSource {
        return DeviceDataSourceImpl(executor)
    }

    fun createDeviceRepository(dataSource: DeviceDataSource): DeviceRepository {
        return DeviceRepositoryImpl(dataSource)
    }

    fun createGetDevicesUseCase(repository: DeviceRepository): GetDevicesUseCase {
        return GetDevicesUseCase(repository)
    }

    fun createGetDeviceInfoUseCase(repository: DeviceRepository): GetDeviceInfoUseCase {
        return GetDeviceInfoUseCase(repository)
    }
}
