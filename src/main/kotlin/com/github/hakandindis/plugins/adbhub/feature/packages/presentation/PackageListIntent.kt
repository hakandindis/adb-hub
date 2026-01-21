package com.github.hakandindis.plugins.adbhub.feature.packages.presentation

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Intent actions for Package List feature (MVI pattern)
 */
sealed class PackageListIntent {
    /**
     * Set search text
     */
    data class SetSearchText(val text: String) : PackageListIntent()

    /**
     * Set show system apps filter
     */
    data class SetShowSystemApps(val show: Boolean) : PackageListIntent()

    /**
     * Set show user apps filter
     */
    data class SetShowUserApps(val show: Boolean) : PackageListIntent()

    /**
     * Set show debug apps filter
     */
    data class SetShowDebugApps(val show: Boolean) : PackageListIntent()

    /**
     * Select a package
     */
    data class SelectPackage(val packageItem: ApplicationPackage) : PackageListIntent()

    /**
     * Refresh packages list
     */
    data class RefreshPackages(val deviceId: String, val includeSystemApps: Boolean = true) : PackageListIntent()
}
