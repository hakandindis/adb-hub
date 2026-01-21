package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PathItemUiModel
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Mapper for converting PackageDetails to UI models for General Info tab
 */
object GeneralInfoMapper {
    /**
     * Converts PackageDetails to list of InfoItemUiModel for general information
     */
    fun toInfoItems(packageDetails: PackageDetails): List<InfoItemUiModel> {
        return listOf(
            InfoItemUiModel(
                label = "App Name",
                value = extractAppName(packageDetails.packageName)
            ),
            InfoItemUiModel(
                label = "Package",
                value = packageDetails.packageName
            ),
            InfoItemUiModel(
                label = "Version Name",
                value = packageDetails.versionName ?: "N/A"
            ),
            InfoItemUiModel(
                label = "Version Code",
                value = packageDetails.versionCode ?: "N/A"
            ),
            InfoItemUiModel(
                label = "Min SDK",
                value = packageDetails.minSdkVersion?.let {
                    "$it (Android ${getAndroidVersionName(it)})"
                } ?: "N/A"
            ),
            InfoItemUiModel(
                label = "Target SDK",
                value = packageDetails.targetSdkVersion?.let {
                    "$it (Android ${getAndroidVersionName(it)})"
                } ?: "N/A"
            ),
            InfoItemUiModel(
                label = "System App",
                value = if (packageDetails.isSystemApp) "Yes" else "No"
            ),
            InfoItemUiModel(
                label = "Enabled",
                value = if (packageDetails.isEnabled) "Yes" else "No"
            ),
            InfoItemUiModel(
                label = "First Install",
                value = packageDetails.firstInstallTime ?: "N/A"
            ),
            InfoItemUiModel(
                label = "Last Update",
                value = packageDetails.lastUpdateTime ?: "N/A"
            )
        )
    }

    /**
     * Converts PackageDetails to list of PathItemUiModel for paths section
     */
    fun toPathItems(packageDetails: PackageDetails): List<PathItemUiModel> {
        val items = mutableListOf<PathItemUiModel>()

        packageDetails.installLocation?.let {
            items.add(PathItemUiModel(label = "APK Path", path = it))
        }

        packageDetails.dataDirectory?.let {
            items.add(PathItemUiModel(label = "Data Directory", path = it))
        }

        return items
    }

    /**
     * Extracts app name from package name
     */
    private fun extractAppName(packageName: String): String {
        return packageName.substringAfterLast(".").takeIf { it.isNotEmpty() }
            ?: packageName
    }

    /**
     * Gets Android version name from SDK version
     */
    private fun getAndroidVersionName(sdkVersion: String): String {
        val version = sdkVersion.toIntOrNull() ?: return "Unknown"
        return when (version) {
            34 -> "14"
            33 -> "13"
            32 -> "12L"
            31 -> "12"
            30 -> "11"
            29 -> "10"
            28 -> "9"
            27 -> "8.1"
            26 -> "8.0"
            25 -> "7.1"
            24 -> "7.0"
            23 -> "6.0"
            22 -> "5.1"
            21 -> "5.0"
            else -> "Unknown"
        }
    }
}
