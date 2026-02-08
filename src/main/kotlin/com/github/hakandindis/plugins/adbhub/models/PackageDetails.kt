package com.github.hakandindis.plugins.adbhub.models

/**
 * Detailed information about an Android package/application
 */
data class PackageDetails(
    val packageName: String,
    val versionName: String? = null,
    val versionCode: String? = null,
    val installLocation: String? = null,
    val dataDirectory: String? = null,
    val permissionSections: List<PermissionSection> = emptyList(),
    val activities: List<ActivityInfo> = emptyList(),
    val services: List<ServiceInfo> = emptyList(),
    val receivers: List<ReceiverInfo> = emptyList(),
    val contentProviders: List<ProviderInfo> = emptyList(),
    val isSystemApp: Boolean = false,
    val isEnabled: Boolean = true,
    val firstInstallTime: String? = null,
    val lastUpdateTime: String? = null,
    val targetSdkVersion: String? = null,
    val minSdkVersion: String? = null
) {
    data class ActivityInfo(
        val name: String,
        val exported: Boolean = false,
        val enabled: Boolean = true,
        val intentFilters: List<IntentFilter> = emptyList()
    ) {
        data class IntentFilter(
            val actions: List<String> = emptyList(),
            val categories: List<String> = emptyList(),
            val data: List<String> = emptyList()
        )
    }

    data class ServiceInfo(
        val name: String,
        val exported: Boolean = false,
        val enabled: Boolean = true
    )

    data class ReceiverInfo(
        val name: String,
        val exported: Boolean = false,
        val enabled: Boolean = true
    )

    data class ProviderInfo(
        val name: String,
        val exported: Boolean = false,
        val enabled: Boolean = true
    )
}
