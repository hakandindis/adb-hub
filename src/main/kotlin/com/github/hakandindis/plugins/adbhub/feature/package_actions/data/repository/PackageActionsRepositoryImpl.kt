package com.github.hakandindis.plugins.adbhub.feature.package_actions.data.repository

import com.github.hakandindis.plugins.adbhub.constants.*
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubError
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.core.result.toAdbHubResult
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository
import com.intellij.openapi.diagnostic.Logger

class PackageActionsRepositoryImpl(
    private val commandExecutor: AdbCommandExecutor
) : PackageActionsRepository {

    private val logger = Logger.getInstance(PackageActionsRepositoryImpl::class.java)

    override suspend fun launchApp(packageName: String, deviceId: String): AdbHubResult<Unit> {
        return execute(deviceId, MonkeyCommands.launchApp(packageName)) {
            logger.info("App launched: $packageName")
        }
    }

    override suspend fun launchActivity(activityName: String, deviceId: String): AdbHubResult<Unit> {
        return execute(deviceId, AmCommands.startActivity(activityName)) {
            logger.info("Activity launched: $activityName")
        }
    }

    override suspend fun forceStop(packageName: String, deviceId: String): AdbHubResult<Unit> {
        return execute(deviceId, AmCommands.forceStop(packageName)) {
            logger.info("App force stopped: $packageName")
        }
    }

    override suspend fun clearData(packageName: String, deviceId: String): AdbHubResult<Unit> {
        return execute(deviceId, PmCommands.clearData(packageName)) {
            logger.info("App data cleared: $packageName")
        }
    }

    override suspend fun uninstall(packageName: String, deviceId: String): AdbHubResult<Unit> {
        return execute(deviceId, PmCommands.uninstall(packageName)) {
            logger.info("App uninstalled: $packageName")
        }
    }

    override suspend fun launchDeepLink(uri: String, deviceId: String): AdbHubResult<Unit> {
        return try {
            val command = AmCommands.startActivityWithAction(IntentConstants.Actions.VIEW, uri)
            when (val result = commandExecutor.executeCommandForDevice(deviceId, command).toAdbHubResult()) {
                is AdbHubResult.Success -> {
                    logger.info("Deep link launched: $uri")
                    AdbHubResult.success(Unit)
                }

                is AdbHubResult.Failure -> {
                    logger.error("Failed to launch deep link: ${result.error.toUserMessage()}")
                    AdbHubResult.failure(result.error)
                }
            }
        } catch (e: Exception) {
            logger.error("Error launching deep link $uri", e)
            AdbHubResult.failure(AdbHubError.Unknown(e.message ?: "Failed to launch deep link", e))
        }
    }

    override suspend fun setStayAwake(enabled: Boolean, deviceId: String): AdbHubResult<Unit> {
        return execute(deviceId, SettingsCommands.setStayAwake(enabled)) {
            logger.info("Stay awake set to: $enabled")
        }
    }

    override suspend fun setPackageEnabled(
        packageName: String,
        enabled: Boolean,
        deviceId: String
    ): AdbHubResult<Unit> {
        return try {
            val command = if (enabled) PmCommands.enablePackage(packageName) else PmCommands.disablePackage(packageName)
            when (val result = commandExecutor.executeCommandForDevice(deviceId, command).toAdbHubResult()) {
                is AdbHubResult.Success -> {
                    logger.info("Package $packageName ${if (enabled) "enabled" else "disabled"}")
                    AdbHubResult.success(Unit)
                }

                is AdbHubResult.Failure -> {
                    logger.error("Failed to ${if (enabled) "enable" else "disable"} package: ${result.error.toUserMessage()}")
                    AdbHubResult.failure(result.error)
                }
            }
        } catch (e: Exception) {
            logger.error("Error ${if (enabled) "enabling" else "disabling"} package $packageName", e)
            AdbHubResult.failure(
                AdbHubError.Unknown(
                    e.message ?: "Failed to ${if (enabled) "enable" else "disable"} package", e
                )
            )
        }
    }

    private suspend fun execute(deviceId: String, command: String, onSuccess: () -> Unit): AdbHubResult<Unit> {
        return try {
            when (val result = commandExecutor.executeCommandForDevice(deviceId, command).toAdbHubResult()) {
                is AdbHubResult.Success -> {
                    onSuccess()
                    AdbHubResult.success(Unit)
                }

                is AdbHubResult.Failure -> {
                    logger.error("Command failed: ${result.error.toUserMessage()}")
                    AdbHubResult.failure(result.error)
                }
            }
        } catch (e: Exception) {
            logger.error("Command error", e)
            AdbHubResult.failure(AdbHubError.Unknown(e.message ?: "Command failed", e))
        }
    }
}
