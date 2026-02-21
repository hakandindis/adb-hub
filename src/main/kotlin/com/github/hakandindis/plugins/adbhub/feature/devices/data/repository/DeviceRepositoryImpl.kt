package com.github.hakandindis.plugins.adbhub.feature.devices.data.repository

import com.github.hakandindis.plugins.adbhub.constants.AdbCommands
import com.github.hakandindis.plugins.adbhub.constants.DeviceProperties
import com.github.hakandindis.plugins.adbhub.constants.GetpropCommands
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubError
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.core.result.toAdbHubResultWithOutput
import com.github.hakandindis.plugins.adbhub.feature.devices.domain.repository.DeviceRepository
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo
import com.intellij.openapi.diagnostic.Logger

class DeviceRepositoryImpl(
    private val commandExecutor: AdbCommandExecutor
) : DeviceRepository {

    private val logger = Logger.getInstance(DeviceRepositoryImpl::class.java)

    override suspend fun getDevices(): AdbHubResult<List<Device>> {
        return try {
            commandExecutor.executeCommand(AdbCommands.DEVICES)
                .toAdbHubResultWithOutput()
                .map { parseDevices(it) }
        } catch (e: Exception) {
            logger.error("Error getting devices", e)
            AdbHubResult.failure(AdbHubError.Unknown(e.message ?: "Failed to get devices", e))
        }
    }

    override suspend fun getDeviceInfo(deviceId: String): AdbHubResult<DeviceInfo> {
        return try {
            val info = fetchDeviceInfo(deviceId)
            if (info != null) {
                AdbHubResult.success(info)
            } else {
                AdbHubResult.failure(AdbHubError.DeviceNotFound(deviceId))
            }
        } catch (e: Exception) {
            logger.error("Error getting device info for $deviceId", e)
            AdbHubResult.failure(AdbHubError.Unknown(e.message ?: "Failed to get device info", e))
        }
    }

    private fun fetchDeviceInfo(deviceId: String): DeviceInfo? {
        return try {
            val propsResult = commandExecutor.executeCommandForDevice(deviceId, GetpropCommands.getAllProperties())
            val properties = if (propsResult.isSuccess) parseGetpropOutput(propsResult.output) else emptyMap()

            val screenResult = commandExecutor.executeCommandForDevice(deviceId, "shell wm size; wm density")
            val (screenResolution, screenDensity) = if (screenResult.isSuccess) {
                parseScreenInfo(screenResult.output)
            } else Pair(null, null)

            DeviceInfo(
                deviceId = deviceId,
                model = properties[DeviceProperties.MODEL],
                manufacturer = properties[DeviceProperties.MANUFACTURER],
                product = properties[DeviceProperties.PRODUCT],
                androidVersion = properties[DeviceProperties.ANDROID_VERSION],
                apiLevel = properties[DeviceProperties.API_LEVEL],
                sdkVersion = properties[DeviceProperties.API_LEVEL],
                buildNumber = properties[DeviceProperties.BUILD_NUMBER],
                buildFingerprint = properties[DeviceProperties.BUILD_FINGERPRINT],
                screenResolution = screenResolution,
                screenDensity = screenDensity,
                cpuAbi = properties[DeviceProperties.CPU_ABI],
                hardware = properties[DeviceProperties.HARDWARE]
            )
        } catch (e: Exception) {
            logger.error("Error getting device info for $deviceId", e)
            null
        }
    }

    private fun parseGetpropOutput(output: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val pattern = Regex("\\[([^]]+)]:\\s*\\[([^]]*)]")
        for (line in output.lines()) {
            pattern.find(line)?.let { match ->
                val key = match.groupValues[1].trim()
                val value = match.groupValues[2].trim()
                if (value.isNotEmpty()) result[key] = value
            }
        }
        return result
    }

    private fun parseScreenInfo(output: String): Pair<String?, String?> {
        val resolution =
            Regex("Physical size: (.+)").find(output)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotEmpty() }
        val density =
            Regex("Physical density: (.+)").find(output)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotEmpty() }
        return Pair(resolution, density)
    }

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

}
