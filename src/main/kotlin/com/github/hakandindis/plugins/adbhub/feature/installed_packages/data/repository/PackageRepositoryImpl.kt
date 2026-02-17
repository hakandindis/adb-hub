package com.github.hakandindis.plugins.adbhub.feature.installed_packages.data.repository

import com.github.hakandindis.plugins.adbhub.constants.AdbCommands
import com.github.hakandindis.plugins.adbhub.constants.AdbConfig
import com.github.hakandindis.plugins.adbhub.constants.SystemPaths
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubError
import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.intellij.openapi.diagnostic.Logger

class PackageRepositoryImpl(
    private val commandExecutor: AdbCommandExecutor
) : PackageRepository {

    private val logger = Logger.getInstance(PackageRepositoryImpl::class.java)

    override suspend fun getPackages(
        deviceId: String,
        includeSystemApps: Boolean
    ): AdbHubResult<List<ApplicationPackage>> {
        return try {
            fetchPackages(deviceId, includeSystemApps)
        } catch (e: Exception) {
            logger.error("Error getting packages for device $deviceId", e)
            AdbHubResult.failure(AdbHubError.Unknown(e.message ?: "Failed to get packages", e))
        }
    }

    private suspend fun fetchPackages(
        deviceId: String,
        includeSystemApps: Boolean
    ): AdbHubResult<List<ApplicationPackage>> {
        val command = if (includeSystemApps) {
            AdbCommands.LIST_PACKAGES
        } else {
            AdbCommands.LIST_PACKAGES_USER
        }

        val result = commandExecutor.executeCommandForDevice(deviceId, command)

        if (!result.isSuccess) {
            logger.error("Failed to get packages: ${result.error ?: result.output}")
            logger.info("Trying fallback command without -f flag")
            val fallbackCommand = if (includeSystemApps) {
                AdbCommands.LIST_PACKAGES_NO_PATH
            } else {
                AdbCommands.LIST_PACKAGES_USER_NO_PATH
            }
            val fallbackResult = commandExecutor.executeCommandForDevice(deviceId, fallbackCommand)
            return if (fallbackResult.isSuccess) {
                AdbHubResult.success(parsePackages(fallbackResult.output))
            } else {
                AdbHubResult.failure(
                    AdbHubError.AdbCommandFailed(
                        command = command,
                        exitCode = result.exitCode,
                        stderr = result.error?.takeIf { it.isNotBlank() },
                        stdout = result.output.takeIf { it.isNotBlank() }
                    )
                )
            }
        }

        val outputLines = result.output.lines()
        var packages = parsePackages(result.output)
        logger.info("Found ${packages.size} packages for device $deviceId (from ${outputLines.size} lines)")

        if (logger.isDebugEnabled && outputLines.isNotEmpty()) {
            logger.debug("First ${AdbConfig.DEBUG_LINES_COUNT} lines of package list output:")
            outputLines.take(AdbConfig.DEBUG_LINES_COUNT).forEach { logger.debug("  $it") }
        }

        if (packages.size < AdbConfig.PACKAGE_COUNT_THRESHOLD && includeSystemApps) {
            logger.info("Found very few packages, trying without -f flag to get more")
            val fallbackResult = commandExecutor.executeCommandForDevice(deviceId, AdbCommands.LIST_PACKAGES_NO_PATH)
            if (fallbackResult.isSuccess) {
                val fallbackPackages = parsePackages(fallbackResult.output)
                val existingNames = packages.map { it.packageName }.toSet()
                val newPackages = fallbackPackages.filter { it.packageName !in existingNames }
                if (newPackages.isNotEmpty()) {
                    logger.info("Found ${newPackages.size} additional packages without -f flag")
                    packages = (packages + newPackages).sortedBy { it.packageName }
                }
            }
        }

        return AdbHubResult.success(packages)
    }

    private fun parsePackages(output: String): List<ApplicationPackage> {
        val packages = mutableListOf<ApplicationPackage>()
        val lines = output.lines()
        var skippedCount = 0

        for (line in lines) {
            val trimmed = line.trim()

            if (trimmed.isEmpty() || !trimmed.startsWith("package:")) {
                if (trimmed.isNotEmpty()) {
                    logger.debug("Skipping line (doesn't start with 'package:'): $trimmed")
                }
                continue
            }

            val packageInfo = trimmed.substringAfter("package:")

            if (packageInfo.isEmpty()) {
                skippedCount++
                continue
            }

            val parts = if (packageInfo.contains("=")) {
                val lastEqualsIndex = packageInfo.lastIndexOf("=")
                if (lastEqualsIndex > 0 && lastEqualsIndex < packageInfo.length - 1) {
                    listOf(
                        packageInfo.substring(0, lastEqualsIndex),
                        packageInfo.substring(lastEqualsIndex + 1)
                    )
                } else {
                    skippedCount++
                    logger.debug("Skipping line (invalid '=' position): $trimmed")
                    continue
                }
            } else {
                listOf("", packageInfo)
            }

            if (parts.size < 2 || parts[1].isEmpty()) {
                skippedCount++
                logger.debug("Skipping line (invalid format): $trimmed")
                continue
            }

            val apkPath = parts[0].trim()
            val packageName = parts[1].trim()

            if (packageName.isEmpty()) {
                skippedCount++
                continue
            }

            if (!packageName.matches(Regex("^[a-zA-Z][a-zA-Z0-9_.]*$"))) {
                skippedCount++
                logger.debug("Skipping package (invalid name format): $packageName")
                continue
            }

            val isSystemApp =
                apkPath.isNotEmpty() && SystemPaths.SYSTEM_APP_PATHS.any { systemPath -> apkPath.contains(systemPath) }

            packages.add(
                ApplicationPackage(
                    packageName = packageName,
                    versionName = null,
                    versionCode = null,
                    installLocation = apkPath.ifEmpty { null },
                    isSystemApp = isSystemApp,
                    isEnabled = true
                )
            )
        }

        if (skippedCount > 0) {
            logger.debug("Skipped $skippedCount invalid package entries")
        }

        return packages.sortedBy { it.packageName }
    }
}
