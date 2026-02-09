package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

sealed class PackageDetailsIntent {
    data class LoadPackageDetails(val packageName: String, val deviceId: String) : PackageDetailsIntent()
    data class FilterPermissions(val query: String) : PackageDetailsIntent()
    data class FilterActivities(val query: String) : PackageDetailsIntent()
    data class FilterReceivers(val query: String) : PackageDetailsIntent()
    data class FilterServices(val query: String) : PackageDetailsIntent()
    data class FilterContentProviders(val query: String) : PackageDetailsIntent()
}
