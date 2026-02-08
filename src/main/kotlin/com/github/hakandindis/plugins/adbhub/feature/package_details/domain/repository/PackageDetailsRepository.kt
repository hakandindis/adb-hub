package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository

import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Repository interface for package details operations
 */
interface PackageDetailsRepository {
    /**
     * Gets detailed information about a package
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return Result containing PackageDetails or error
     */
    suspend fun getPackageDetails(packageName: String, deviceId: String): Result<PackageDetails>

    /**
     * Gets App Links (domain verification state) for a package.
     * @return Result containing AppLinksInfo or error; null fields if command fails
     */
    suspend fun getAppLinks(packageName: String, deviceId: String): Result<AppLinksInfo?>
}
