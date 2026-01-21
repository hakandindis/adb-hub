package com.github.hakandindis.plugins.adbhub.feature.device.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.device.data.datasource.DeviceDataSource
import com.github.hakandindis.plugins.adbhub.feature.device.data.datasource.DeviceDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.device.data.repository.DeviceRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.device.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.feature.device.domain.usecase.GetDeviceInfoUseCase
import com.github.hakandindis.plugins.adbhub.feature.device.domain.usecase.GetDevicesUseCase

/**
 * Dependency injection module for Device feature
 */
object DeviceModule {
    /**
     * Creates DeviceViewModel dependencies
     */
    fun createDeviceDataSource(executor: AdbCommandExecutor?): DeviceDataSource? {
        return executor?.let { DeviceDataSourceImpl(it) }
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
