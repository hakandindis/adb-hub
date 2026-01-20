package com.github.hakandindis.plugins.adbhub.feature.packages.data.datasource

import com.github.hakandindis.plugins.adbhub.constants.AdbCommands
import com.github.hakandindis.plugins.adbhub.constants.AdbConfig
import com.github.hakandindis.plugins.adbhub.constants.SystemPaths
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of PackageDataSource
 */
class PackageDataSourceImpl(
    private val commandExecutor: AdbCommandExecutor
) : PackageDataSource {

    private val logger = Logger.getInstance(PackageDataSourceImpl::class.java)

    override suspend fun getPackages(deviceId: String, includeSystemApps: Boolean): List<ApplicationPackage> {
        // Use -f flag to get full paths, which helps identify system apps
        // -3 flag shows only third-party apps (user-installed)
        val command = if (includeSystemApps) {
            AdbCommands.LIST_PACKAGES
        } else {
            AdbCommands.LIST_PACKAGES_USER
        }

        val result = commandExecutor.executeCommandForDevice(deviceId, command)

        if (!result.isSuccess) {
            logger.error("Failed to get packages: ${result.error ?: result.output}")
            // Try without -f flag as fallback
            logger.info("Trying fallback command without -f flag")
            val fallbackCommand = if (includeSystemApps) {
                AdbCommands.LIST_PACKAGES_NO_PATH
            } else {
                AdbCommands.LIST_PACKAGES_USER_NO_PATH
            }
            val fallbackResult = commandExecutor.executeCommandForDevice(deviceId, fallbackCommand)
            if (fallbackResult.isSuccess) {
                return parsePackages(fallbackResult.output, deviceId)
            }
            return emptyList()
        }

        val outputLines = result.output.lines()
        val packages = parsePackages(result.output, deviceId)
        logger.info("Found ${packages.size} packages for device $deviceId (from ${outputLines.size} lines)")

        // Log first few lines for debugging only if in debug mode
        if (logger.isDebugEnabled && outputLines.isNotEmpty()) {
            logger.debug("First ${AdbConfig.DEBUG_LINES_COUNT} lines of package list output:")
            outputLines.take(AdbConfig.DEBUG_LINES_COUNT).forEach { logger.debug("  $it") }
        }

        // If we found very few packages, try without -f flag as well and merge
        if (packages.size < AdbConfig.PACKAGE_COUNT_THRESHOLD && includeSystemApps) {
            logger.info("Found very few packages, trying without -f flag to get more")
            val fallbackResult = commandExecutor.executeCommandForDevice(deviceId, AdbCommands.LIST_PACKAGES_NO_PATH)
            if (fallbackResult.isSuccess) {
                val fallbackPackages = parsePackages(fallbackResult.output, deviceId)
                // Merge packages, avoiding duplicates
                val existingNames = packages.map { it.packageName }.toSet()
                val newPackages = fallbackPackages.filter { it.packageName !in existingNames }
                if (newPackages.isNotEmpty()) {
                    logger.info("Found ${newPackages.size} additional packages without -f flag")
                    return (packages + newPackages).sortedBy { it.packageName }
                }
            }
        }

        return packages
    }

    /**
     * Parses package list from 'adb shell pm list packages -f' output
     *
     * Expected formats:
     * - "package:/data/app/com.example.app-1/base.apk=com.example.app"
     * - "package:/system/app/SystemApp.apk=com.android.systemapp"
     * - "package:com.example.app" (if -f flag is not used, but we always use -f)
     */
    private fun parsePackages(output: String, deviceId: String?): List<ApplicationPackage> {
        val packages = mutableListOf<ApplicationPackage>()
        val lines = output.lines()
        var skippedCount = 0

        for (line in lines) {
            val trimmed = line.trim()

            // Skip empty lines and lines that don't start with "package:"
            if (trimmed.isEmpty() || !trimmed.startsWith("package:")) {
                if (trimmed.isNotEmpty()) {
                    logger.debug("Skipping line (doesn't start with 'package:'): $trimmed")
                }
                continue
            }

            // Format: "package:/data/app/com.example.app-1/base.apk=com.example.app"
            // or: "package:com.example.app" (if -f is not used, but we always use -f)
            val packageInfo = trimmed.substringAfter("package:")

            // Skip if package info is empty
            if (packageInfo.isEmpty()) {
                skippedCount++
                continue
            }

            // Check if format has "=" (path=packageName) or just packageName
            // Note: APK paths can contain "=" in some cases, so we need to split from the right
            val parts = if (packageInfo.contains("=")) {
                // Split from the right to handle cases where path contains "="
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
                // If no "=", assume the whole thing is the package name
                // This shouldn't happen with -f flag, but handle it anyway
                listOf("", packageInfo)
            }

            // Need at least package name
            if (parts.size < 2 || parts[1].isEmpty()) {
                skippedCount++
                logger.debug("Skipping line (invalid format): $trimmed")
                continue
            }

            val apkPath = parts[0].trim()
            val packageName = parts[1].trim()

            // Validate package name: must not be empty
            if (packageName.isEmpty()) {
                skippedCount++
                continue
            }

            // Validate package name format: should contain at least one dot
            // (valid Android package names have at least one dot, e.g., "com.example.app")
            // But some system packages might not have dots, so we'll be lenient
            // Only skip if it's clearly invalid (e.g., just whitespace or special chars)
            if (!packageName.matches(Regex("^[a-zA-Z][a-zA-Z0-9_.]*$"))) {
                skippedCount++
                logger.debug("Skipping package (invalid name format): $packageName")
                continue
            }

            // Determine if it's a system app based on path
            val isSystemApp = apkPath?.let { path ->
                SystemPaths.SYSTEM_APP_PATHS.any { systemPath -> path.contains(systemPath) }
            } ?: false

            packages.add(
                ApplicationPackage(
                    packageName = packageName,
                    versionName = null, // Will be loaded on demand if needed
                    versionCode = null,
                    installLocation = apkPath.ifEmpty { null },
                    isSystemApp = isSystemApp,
                    isEnabled = true // Default, can be checked separately if needed
                )
            )
        }

        if (skippedCount > 0) {
            logger.debug("Skipped $skippedCount invalid package entries")
        }

        return packages.sortedBy { it.packageName }
    }
}
