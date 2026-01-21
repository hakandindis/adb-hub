package com.github.hakandindis.plugins.adbhub.feature.device.data.repository

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.device.data.datasource.DeviceDataSource
import com.github.hakandindis.plugins.adbhub.feature.device.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of DeviceRepository
 */
class DeviceRepositoryImpl(
    private val dataSource: DeviceDataSource
) : DeviceRepository {

    private val logger = Logger.getInstance(DeviceRepositoryImpl::class.java)

    override suspend fun getDevices(): Result<List<Device>> {
        return try {
            val devices = dataSource.getDevices()
            Result.success(devices)
        } catch (e: Exception) {
            logger.error("Error getting devices", e)
            Result.failure(e)
        }
    }

    override suspend fun getDeviceInfo(deviceId: String): Result<DeviceInfo> {
        return try {
            val info = dataSource.getDeviceInfo(deviceId)
            if (info != null) {
                Result.success(info)
            } else {
                val error = Exception("Device info not found for device: $deviceId")
                logger.warn("Device info not found", error)
                Result.failure(error)
            }
        } catch (e: Exception) {
            logger.error("Error getting device info for $deviceId", e)
            Result.failure(e)
        }
    }
}
