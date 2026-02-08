package com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource

import com.github.hakandindis.plugins.adbhub.constants.DumpsysCommands
import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns
import com.github.hakandindis.plugins.adbhub.constants.PmCommands
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.*
import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo
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
            val contentProviders = extractContentProviders(dumpsysOutput, packageName)
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
                contentProviders = contentProviders,
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

        val serviceSection = DumpsysParser.extractSection(
            output,
            DumpsysParseStrings.SERVICE_RESOLVER_TABLE,
            listOf(
                DumpsysParseStrings.DOMAIN_VERIFICATION_STATUS,
                DumpsysParseStrings.REGISTERED_CONTENT_PROVIDERS,
                DumpsysParseStrings.PACKAGES_SECTION
            )
        )
        if (serviceSection.isNotEmpty()) {
            ParsePatterns.RESOLVER_TABLE_COMPONENT.findAll(serviceSection).forEach { match ->
                val pkg = match.groupValues[1]
                val className = match.groupValues[2]
                if (pkg == packageName) {
                    services.add(
                        PackageDetails.ServiceInfo(
                            name = "$pkg/$className",
                            exported = true,
                            enabled = true
                        )
                    )
                }
            }
        }

        if (services.isNotEmpty()) {
            return services.distinctBy { it.name }.sortedBy { it.name }
        }

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

        val receiverSection = DumpsysParser.extractSection(
            output,
            DumpsysParseStrings.RECEIVER_RESOLVER_TABLE,
            listOf(DumpsysParseStrings.SERVICE_RESOLVER_TABLE)
        )
        if (receiverSection.isNotEmpty()) {
            ParsePatterns.RESOLVER_TABLE_COMPONENT.findAll(receiverSection).forEach { match ->
                val pkg = match.groupValues[1]
                val className = match.groupValues[2]
                if (pkg == packageName) {
                    receivers.add(
                        PackageDetails.ReceiverInfo(
                            name = "$pkg/$className",
                            exported = true,
                            enabled = true
                        )
                    )
                }
            }
        }

        if (receivers.isNotEmpty()) {
            return receivers.distinctBy { it.name }.sortedBy { it.name }
        }

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

    private fun extractContentProviders(output: String, packageName: String): List<PackageDetails.ProviderInfo> {
        val providers = mutableListOf<PackageDetails.ProviderInfo>()

        val providerSection = DumpsysParser.extractSection(
            output,
            DumpsysParseStrings.REGISTERED_CONTENT_PROVIDERS,
            listOf(
                DumpsysParseStrings.CONTENT_PROVIDER_AUTHORITIES,
                DumpsysParseStrings.DOMAIN_VERIFICATION_STATUS,
                DumpsysParseStrings.PACKAGES_SECTION
            )
        )

        if (providerSection.isNotEmpty()) {
            ParsePatterns.REGISTERED_PROVIDER_LINE.findAll(providerSection).forEach { match ->
                val pkg = match.groupValues[1]
                val className = match.groupValues[2]
                if (pkg == packageName) {
                    providers.add(
                        PackageDetails.ProviderInfo(
                            name = "$pkg/$className",
                            exported = true,
                            enabled = true
                        )
                    )
                }
            }
        }

        if (providers.isNotEmpty()) {
            return providers.distinctBy { it.name }.sortedBy { it.name }
        }

        ParsePatterns.PROVIDER_PATTERN_1.findAll(output).forEach { match ->
            val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
            if (fullName.startsWith(packageName)) {
                val contextStart = (match.range.first - 300).coerceAtLeast(0)
                val contextEnd = (match.range.last + 300).coerceAtMost(output.length)
                val context = output.substring(contextStart, contextEnd)

                val exported = ParsePatterns.EXPORTED_TRUE in context || ParsePatterns.EXPORTED_TRUE_ALT in context
                val enabled = !(ParsePatterns.ENABLED_FALSE in context || ParsePatterns.ENABLED_FALSE_ALT in context)

                providers.add(
                    PackageDetails.ProviderInfo(
                        name = fullName,
                        exported = exported,
                        enabled = enabled
                    )
                )
            }
        }

        if (providers.isEmpty()) {
            ParsePatterns.PROVIDER_PATTERN_2.findAll(output).forEach { match ->
                val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
                if (fullName.startsWith(packageName)) {
                    val contextStart = (match.range.first - 300).coerceAtLeast(0)
                    val contextEnd = (match.range.last + 300).coerceAtMost(output.length)
                    val context = output.substring(contextStart, contextEnd)

                    val exported = "exported=true" in context || "exported: true" in context
                    val enabled = !("enabled=false" in context || "enabled: false" in context)

                    providers.add(
                        PackageDetails.ProviderInfo(
                            name = fullName,
                            exported = exported,
                            enabled = enabled
                        )
                    )
                }
            }
        }

        return providers
            .distinctBy { it.name }
            .sortedBy { it.name }
    }

    override suspend fun getAppLinks(packageName: String, deviceId: String): AppLinksInfo? {
        return try {
            val result = commandExecutor.executeCommandForDevice(deviceId, PmCommands.getAppLinks(packageName))
            if (!result.isSuccess) {
                logger.warn("Failed to get app links for $packageName: ${result.error}")
                return null
            }
            AppLinksParser.parse(result.output, packageName)
        } catch (e: Exception) {
            logger.error("Error getting app links for $packageName", e)
            null
        }
    }
}
