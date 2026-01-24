package com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation

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
     * Select a package
     */
    data class SelectPackage(val packageItem: ApplicationPackage) : PackageListIntent()

    /**
     * Refresh packages list
     */
    data class RefreshPackages(val deviceId: String, val includeSystemApps: Boolean = true) : PackageListIntent()
}
