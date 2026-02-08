package com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource

import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Data source interface for package details operations
 */
interface PackageDetailsDataSource {
    /**
     * Gets detailed information about a package
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return PackageDetails with all package information
     */
    suspend fun getPackageDetails(packageName: String, deviceId: String): PackageDetails?

    /**
     * Gets App Links (domain verification state) for a package.
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return AppLinksInfo or null if command fails / no data
     */
    suspend fun getAppLinks(packageName: String, deviceId: String): AppLinksInfo?
}
