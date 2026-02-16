package com.github.hakandindis.plugins.adbhub.feature.package_actions.data.repository

import com.github.hakandindis.plugins.adbhub.constants.*
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository
import com.intellij.openapi.diagnostic.Logger

class PackageActionsRepositoryImpl(
    private val commandExecutor: AdbCommandExecutor
) : PackageActionsRepository {

    private val logger = Logger.getInstance(PackageActionsRepositoryImpl::class.java)

    override suspend fun launchApp(packageName: String, deviceId: String): Result<Unit> {
        return execute(deviceId, MonkeyCommands.launchApp(packageName)) {
            logger.info("App launched: $packageName")
        }
    }

    override suspend fun launchActivity(activityName: String, deviceId: String): Result<Unit> {
        return execute(deviceId, AmCommands.startActivity(activityName)) {
            logger.info("Activity launched: $activityName")
        }
    }

    override suspend fun forceStop(packageName: String, deviceId: String): Result<Unit> {
        return execute(deviceId, AmCommands.forceStop(packageName)) {
            logger.info("App force stopped: $packageName")
        }
    }

    override suspend fun clearData(packageName: String, deviceId: String): Result<Unit> {
        return execute(deviceId, PmCommands.clearData(packageName)) {
            logger.info("App data cleared: $packageName")
        }
    }

    override suspend fun clearCache(packageName: String, deviceId: String): Result<Unit> {
        return execute(deviceId, PmCommands.clearCache(packageName)) {
            logger.info("App cache cleared: $packageName")
        }
    }

    override suspend fun uninstall(packageName: String, deviceId: String): Result<Unit> {
        return execute(deviceId, PmCommands.uninstall(packageName)) {
            logger.info("App uninstalled: $packageName")
        }
    }

    override suspend fun launchDeepLink(uri: String, deviceId: String): Result<Unit> {
        return try {
            val command = AmCommands.startActivityWithAction(IntentConstants.Actions.VIEW, uri)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("Deep link launched: $uri")
                Result.success(Unit)
            } else {
                logger.error("Failed to launch deep link: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error launching deep link $uri", e)
            Result.failure(e)
        }
    }

    override suspend fun setStayAwake(enabled: Boolean, deviceId: String): Result<Unit> {
        return execute(deviceId, SettingsCommands.setStayAwake(enabled)) {
            logger.info("Stay awake set to: $enabled")
        }
    }

    override suspend fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String): Result<Unit> {
        return try {
            val command = if (enabled) PmCommands.enablePackage(packageName) else PmCommands.disablePackage(packageName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("Package $packageName ${if (enabled) "enabled" else "disabled"}")
                Result.success(Unit)
            } else {
                logger.error("Failed to ${if (enabled) "enable" else "disable"} package: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error ${if (enabled) "enabling" else "disabling"} package $packageName", e)
            Result.failure(e)
        }
    }

    private suspend fun execute(deviceId: String, command: String, onSuccess: () -> Unit): Result<Unit> {
        return try {
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                onSuccess()
                Result.success(Unit)
            } else {
                logger.error("Command failed: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Command error", e)
            Result.failure(e)
        }
    }
}
