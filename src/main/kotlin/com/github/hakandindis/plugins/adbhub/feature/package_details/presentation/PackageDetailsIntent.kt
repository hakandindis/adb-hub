package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

/**
 * Intent actions for Package Details feature (MVI pattern)
 */
sealed class PackageDetailsIntent {
    /**
     * Load package details
     */
    data class LoadPackageDetails(val packageName: String, val deviceId: String) : PackageDetailsIntent()

    /**
     * Launch an activity
     */
    data class LaunchActivity(val activityName: String, val deviceId: String) : PackageDetailsIntent()

    /**
     * Filter permissions by search query
     */
    data class FilterPermissions(val query: String) : PackageDetailsIntent()

    /**
     * Filter activities by search query
     */
    data class FilterActivities(val query: String) : PackageDetailsIntent()

    /**
     * Filter receivers by search query
     */
    data class FilterReceivers(val query: String) : PackageDetailsIntent()

    /**
     * Filter services by search query
     */
    data class FilterServices(val query: String) : PackageDetailsIntent()

    /**
     * Filter content providers by search query
     */
    data class FilterContentProviders(val query: String) : PackageDetailsIntent()
}
