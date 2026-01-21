package com.github.hakandindis.plugins.adbhub.feature.device.data.datasource

import com.github.hakandindis.plugins.adbhub.constants.AdbCommands
import com.github.hakandindis.plugins.adbhub.constants.DeviceProperties
import com.github.hakandindis.plugins.adbhub.constants.GetpropCommands
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of DeviceDataSource
 */
class DeviceDataSourceImpl(
    private val commandExecutor: AdbCommandExecutor
) : DeviceDataSource {

    private val logger = Logger.getInstance(DeviceDataSourceImpl::class.java)

    override suspend fun getDevices(): List<Device> {
        val result = commandExecutor.executeCommand(AdbCommands.DEVICES)
        return parseDevices(result.output)
    }

    override suspend fun getDeviceInfo(deviceId: String): DeviceInfo? {
        return try {
            val apiLevel = getProperty(deviceId, DeviceProperties.API_LEVEL)
            val manufacturer = getProperty(deviceId, DeviceProperties.MANUFACTURER)
            val model = getProperty(deviceId, DeviceProperties.MODEL)
            val product = getProperty(deviceId, DeviceProperties.PRODUCT)
            val androidVersion = getProperty(deviceId, DeviceProperties.ANDROID_VERSION)
            val buildNumber = getProperty(deviceId, DeviceProperties.BUILD_NUMBER)
            val buildFingerprint = getProperty(deviceId, DeviceProperties.BUILD_FINGERPRINT)
            val hardware = getProperty(deviceId, DeviceProperties.HARDWARE)
            val cpuAbi = getProperty(deviceId, DeviceProperties.CPU_ABI)
            val screenResolution = getScreenInfo(deviceId, "wm size", "Physical size: ")
            val screenDensity = getScreenInfo(deviceId, "wm density", "Physical density: ")

            DeviceInfo(
                deviceId = deviceId,
                model = model,
                manufacturer = manufacturer,
                product = product,
                androidVersion = androidVersion,
                apiLevel = apiLevel,
                sdkVersion = apiLevel,
                buildNumber = buildNumber,
                buildFingerprint = buildFingerprint,
                screenResolution = screenResolution,
                screenDensity = screenDensity,
                cpuAbi = cpuAbi,
                hardware = hardware
            )
        } catch (e: Exception) {
            logger.error("Error getting device info for $deviceId", e)
            null
        }
    }

    /**
     * Parses device list from 'adb devices -l' output
     */
    private fun parseDevices(output: String): List<Device> {
        val devices = mutableListOf<Device>()
        val lines = output.lines()

        for (i in 1 until lines.size) {
            val line = lines[i].trim()
            if (line.isBlank()) continue

            val parts = line.split("\\s+".toRegex())
            if (parts.size < 2) continue

            val deviceId = parts[0]
            val state = parseDeviceState(parts[1])

            var model: String? = null
            var product: String? = null
            var transportId: String? = null

            for (j in 2 until parts.size) {
                val part = parts[j]
                when {
                    part.startsWith("model:") -> model = part.substringAfter("model:")
                    part.startsWith("product:") -> product = part.substringAfter("product:")
                    part.startsWith("transport_id:") -> transportId = part.substringAfter("transport_id:")
                }
            }

            devices.add(Device(deviceId, state, model, product, transportId))
        }

        return devices
    }

    private fun parseDeviceState(state: String): DeviceState {
        return when (state.uppercase()) {
            "DEVICE" -> DeviceState.DEVICE
            "OFFLINE" -> DeviceState.OFFLINE
            "UNAUTHORIZED" -> DeviceState.UNAUTHORIZED
            else -> DeviceState.UNKNOWN
        }
    }

    fun getProperty(deviceId: String, property: String): String? {
        val result = commandExecutor.executeCommandForDevice(deviceId, GetpropCommands.getProperty(property))
        return if (result.isSuccess) {
            result.output.trim().takeIf { it.isNotBlank() }
        } else {
            logger.debug("Failed to get property $property: ${result.error}")
            null
        }
    }

    fun getScreenInfo(deviceId: String, command: String, prefix: String): String? {
        val result = commandExecutor.executeCommandForDevice(deviceId, "shell $command")
        return if (result.isSuccess) {
            result.output.trim().substringAfter(prefix).takeIf { it.isNotBlank() }
        } else {
            logger.debug("Failed to get screen info: ${result.error}")
            null
        }
    }

}
