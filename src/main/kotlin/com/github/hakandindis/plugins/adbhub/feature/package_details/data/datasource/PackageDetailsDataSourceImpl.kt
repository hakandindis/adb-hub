package com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource

import com.github.hakandindis.plugins.adbhub.constants.DumpsysCommands
import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns
import com.github.hakandindis.plugins.adbhub.constants.PmCommands
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.ActivitiesParser
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.DumpsysParser
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.PackageDetailsParser
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.PermissionsParser
import com.github.hakandindis.plugins.adbhub.models.PackageDetails
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of PackageDetailsDataSource
 */
class PackageDetailsDataSourceImpl(
    private val commandExecutor: AdbCommandExecutor
) : PackageDetailsDataSource {

    private val logger = Logger.getInstance(PackageDetailsDataSourceImpl::class.java)

    override suspend fun getPackageDetails(packageName: String, deviceId: String): PackageDetails? {
        return try {
            val dumpsysResult =
                commandExecutor.executeCommandForDevice(deviceId, DumpsysCommands.getPackageDumpsys(packageName))
            if (!dumpsysResult.isSuccess) {
                logger.warn("Failed to get dumpsys package info: ${dumpsysResult.error}")
                return null
            }

            val dumpsysOutput = dumpsysResult.output

            val pathResult = commandExecutor.executeCommandForDevice(deviceId, PmCommands.getPackagePath(packageName))
            val installLocation = if (pathResult.isSuccess) {
                pathResult.output.lines()
                    .firstOrNull { it.startsWith(DumpsysParseStrings.PACKAGE_PREFIX) }
                    ?.substringAfter(DumpsysParseStrings.PACKAGE_PREFIX)
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
            } else {
                null
            }

            val versionName = DumpsysParser.extractVersionName(dumpsysOutput)
            val versionCode = DumpsysParser.extractVersionCode(dumpsysOutput)
            val dataDirectory = DumpsysParser.extractDataDirectory(dumpsysOutput)
            val permissionSections = PermissionsParser.parsePermissionSections(dumpsysOutput)
            val activities = ActivitiesParser.extractActivities(dumpsysOutput, packageName)
            val services = extractServices(dumpsysOutput, packageName)
            val receivers = extractReceivers(dumpsysOutput, packageName)
            val isSystemApp = PackageDetailsParser.isSystemApp(installLocation)
            val isEnabled = PackageDetailsParser.extractEnabledState(dumpsysOutput)
            val firstInstallTime = PackageDetailsParser.extractFirstInstallTime(dumpsysOutput)
            val lastUpdateTime = PackageDetailsParser.extractLastUpdateTime(dumpsysOutput)
            val targetSdkVersion = DumpsysParser.extractTargetSdkVersion(dumpsysOutput)
            val minSdkVersion = DumpsysParser.extractMinSdkVersion(dumpsysOutput)

            PackageDetails(
                packageName = packageName,
                versionName = versionName,
                versionCode = versionCode,
                installLocation = installLocation,
                dataDirectory = dataDirectory,
                permissionSections = permissionSections,
                activities = activities,
                services = services,
                receivers = receivers,
                isSystemApp = isSystemApp,
                isEnabled = isEnabled,
                firstInstallTime = firstInstallTime,
                lastUpdateTime = lastUpdateTime,
                targetSdkVersion = targetSdkVersion,
                minSdkVersion = minSdkVersion
            )
        } catch (e: Exception) {
            logger.error("Error getting package details for $packageName", e)
            null
        }
    }

    private fun extractServices(
        output: String,
        packageName: String
    ): List<PackageDetails.ServiceInfo> {
        val services = mutableListOf<PackageDetails.ServiceInfo>()

        val servicePattern1 = ParsePatterns.SERVICE_PATTERN_1
        servicePattern1.findAll(output).forEach { match ->
            val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
            if (fullName.startsWith(packageName)) {
                val contextStart = (match.range.first - 300).coerceAtLeast(0)
                val contextEnd = (match.range.last + 300).coerceAtMost(output.length)
                val context = output.substring(contextStart, contextEnd)

                val exported = ParsePatterns.EXPORTED_TRUE in context || ParsePatterns.EXPORTED_TRUE_ALT in context
                val enabled = !(ParsePatterns.ENABLED_FALSE in context || ParsePatterns.ENABLED_FALSE_ALT in context)

                services.add(
                    PackageDetails.ServiceInfo(
                        name = fullName,
                        exported = exported,
                        enabled = enabled
                    )
                )
            }
        }

        if (services.isEmpty()) {
            val servicePattern2 = ParsePatterns.SERVICE_PATTERN_2
            servicePattern2.findAll(output).forEach { match ->
                val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
                if (fullName.startsWith(packageName)) {
                    val contextStart = (match.range.first - 300).coerceAtLeast(0)
                    val contextEnd = (match.range.last + 300).coerceAtMost(output.length)
                    val context = output.substring(contextStart, contextEnd)

                    val exported = "exported=true" in context || "exported: true" in context
                    val enabled = !("enabled=false" in context || "enabled: false" in context)

                    services.add(
                        PackageDetails.ServiceInfo(
                            name = fullName,
                            exported = exported,
                            enabled = enabled
                        )
                    )
                }
            }
        }

        return services.distinctBy { it.name }.sortedBy { it.name }
    }

    private fun extractReceivers(output: String, packageName: String): List<PackageDetails.ReceiverInfo> {
        val receivers = mutableListOf<PackageDetails.ReceiverInfo>()

        val receiverPattern1 = ParsePatterns.RECEIVER_PATTERN_1
        receiverPattern1.findAll(output).forEach { match ->
            val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
            if (fullName.startsWith(packageName)) {
                val contextStart = (match.range.first - 300).coerceAtLeast(0)
                val contextEnd = (match.range.last + 300).coerceAtMost(output.length)
                val context = output.substring(contextStart, contextEnd)

                val exported = ParsePatterns.EXPORTED_TRUE in context || ParsePatterns.EXPORTED_TRUE_ALT in context
                val enabled = !(ParsePatterns.ENABLED_FALSE in context || ParsePatterns.ENABLED_FALSE_ALT in context)

                receivers.add(
                    PackageDetails.ReceiverInfo(
                        name = fullName,
                        exported = exported,
                        enabled = enabled
                    )
                )
            }
        }

        if (receivers.isEmpty()) {
            val receiverPattern2 = ParsePatterns.RECEIVER_PATTERN_2
            receiverPattern2.findAll(output).forEach { match ->
                val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
                if (fullName.startsWith(packageName)) {
                    val contextStart = (match.range.first - 300).coerceAtLeast(0)
                    val contextEnd = (match.range.last + 300).coerceAtMost(output.length)
                    val context = output.substring(contextStart, contextEnd)

                    val exported = "exported=true" in context || "exported: true" in context
                    val enabled = !("enabled=false" in context || "enabled: false" in context)

                    receivers.add(
                        PackageDetails.ReceiverInfo(
                            name = fullName,
                            exported = exported,
                            enabled = enabled
                        )
                    )
                }
            }
        }

        return receivers.distinctBy { it.name }.sortedBy { it.name }
    }
}
