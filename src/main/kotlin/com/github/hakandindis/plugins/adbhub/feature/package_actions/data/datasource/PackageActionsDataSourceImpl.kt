package com.github.hakandindis.plugins.adbhub.feature.package_actions.data.datasource

import com.github.hakandindis.plugins.adbhub.constants.*
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of PackageActionsDataSource
 */
class PackageActionsDataSourceImpl(
    private val commandExecutor: AdbCommandExecutor
) : PackageActionsDataSource {

    private val logger = Logger.getInstance(PackageActionsDataSourceImpl::class.java)

    override suspend fun launchApp(packageName: String, deviceId: String): Result<Unit> {
        return try {
            val command = MonkeyCommands.launchApp(packageName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("App launched: $packageName")
                Result.success(Unit)
            } else {
                logger.error("Failed to launch app: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error launching app $packageName", e)
            Result.failure(e)
        }
    }

    override suspend fun launchActivity(activityName: String, deviceId: String): Result<Unit> {
        return try {
            val command = AmCommands.startActivity(activityName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("Activity launched: $activityName")
                Result.success(Unit)
            } else {
                logger.error("Failed to launch activity: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error launching activity $activityName", e)
            Result.failure(e)
        }
    }

    override suspend fun forceStop(packageName: String, deviceId: String): Result<Unit> {
        return try {
            val command = AmCommands.forceStop(packageName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("App force stopped: $packageName")
                Result.success(Unit)
            } else {
                logger.error("Failed to force stop app: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error force stopping app $packageName", e)
            Result.failure(e)
        }
    }

    override suspend fun clearData(packageName: String, deviceId: String): Result<Unit> {
        return try {
            val command = PmCommands.clearData(packageName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("App data cleared: $packageName")
                Result.success(Unit)
            } else {
                logger.error("Failed to clear data: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error clearing data for $packageName", e)
            Result.failure(e)
        }
    }

    override suspend fun clearCache(packageName: String, deviceId: String): Result<Unit> {
        return try {
            val command = PmCommands.clearCache(packageName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("App cache cleared: $packageName")
                Result.success(Unit)
            } else {
                logger.error("Failed to clear cache: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error clearing cache for $packageName", e)
            Result.failure(e)
        }
    }

    override suspend fun uninstall(packageName: String, deviceId: String): Result<Unit> {
        return try {
            val command = PmCommands.uninstall(packageName)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("App uninstalled: $packageName")
                Result.success(Unit)
            } else {
                logger.error("Failed to uninstall app: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error uninstalling app $packageName", e)
            Result.failure(e)
        }
    }

    override suspend fun launchDeepLink(uri: String, packageName: String, deviceId: String): Result<Unit> {
        return try {
            val command = AmCommands.startActivityWithAction(
                IntentConstants.Actions.VIEW,
                uri,
                packageName
            )
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("Deep link launched: $uri for $packageName")
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
        return try {
            val command = SettingsCommands.setStayAwake(enabled)
            val result = commandExecutor.executeCommandForDevice(deviceId, command)
            if (result.isSuccess) {
                logger.info("Stay awake set to: $enabled")
                Result.success(Unit)
            } else {
                logger.error("Failed to set stay awake: ${result.error ?: result.output}")
                Result.failure(Exception(result.error ?: result.output))
            }
        } catch (e: Exception) {
            logger.error("Error setting stay awake", e)
            Result.failure(e)
        }
    }

    override suspend fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String): Result<Unit> {
        return try {
            val command = if (enabled) {
                PmCommands.enablePackage(packageName)
            } else {
                PmCommands.disablePackage(packageName)
            }
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
}
