package com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

sealed class PackageListIntent {
    data class SearchPackages(val query: String) : PackageListIntent()
    data class SelectPackage(val packageItem: ApplicationPackage) : PackageListIntent()
    data class RefreshPackages(val deviceId: String, val includeSystemApps: Boolean = true) : PackageListIntent()
}
