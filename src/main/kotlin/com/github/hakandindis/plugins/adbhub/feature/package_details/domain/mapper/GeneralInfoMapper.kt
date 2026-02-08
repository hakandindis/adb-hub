package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

object GeneralInfoMapper {

    fun toMergedInfoItems(packageDetails: PackageDetails): List<InfoItemUiModel> {
        val items = mutableListOf<InfoItemUiModel>()

        items.add(InfoItemUiModel(label = "Package Name", value = packageDetails.packageName))
        items.add(InfoItemUiModel(label = "App Name", value = extractAppName(packageDetails.packageName)))
        items.add(InfoItemUiModel(label = "Version Number", value = packageDetails.versionName ?: "N/A"))
        items.add(InfoItemUiModel(label = "Version Code", value = packageDetails.versionCode ?: "N/A"))
        items.add(
            InfoItemUiModel(
                label = "Min SDK",
                value = packageDetails.minSdkVersion?.let { "$it (Android ${getAndroidVersionName(it)})" } ?: "N/A"))
        items.add(
            InfoItemUiModel(
                label = "Target SDK",
                value = packageDetails.targetSdkVersion?.let {
                    "$it (Android ${getAndroidVersionName(it)})"
                } ?: "N/A"
            )
        )
        items.add(InfoItemUiModel(label = "Installation Path", value = packageDetails.installLocation ?: "N/A"))
        items.add(InfoItemUiModel(label = "Data Path", value = packageDetails.dataDirectory ?: "N/A"))
        items.add(InfoItemUiModel(label = "Install Time", value = packageDetails.firstInstallTime ?: "N/A"))
        items.add(InfoItemUiModel(label = "System App", value = if (packageDetails.isSystemApp) "Yes" else "No"))
        items.add(InfoItemUiModel(label = "Enabled", value = if (packageDetails.isEnabled) "Yes" else "No"))
        items.add(InfoItemUiModel(label = "Last Update", value = packageDetails.lastUpdateTime ?: "N/A"))

        return items
    }

    private fun extractAppName(packageName: String): String {
        return packageName.substringAfterLast(".").takeIf { it.isNotEmpty() }
            ?: packageName
    }

    private fun getAndroidVersionName(sdkVersion: String): String {
        val version = sdkVersion.toIntOrNull() ?: return "Unknown"
        return when (version) {
            36 -> "16"
            35 -> "15"
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
