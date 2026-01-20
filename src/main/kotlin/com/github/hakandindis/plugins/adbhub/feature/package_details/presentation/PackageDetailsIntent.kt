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
}
