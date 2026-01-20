package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository

import com.github.hakandindis.plugins.adbhub.models.CertificateInfo
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
     * Gets certificate information for a package
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return Result containing CertificateInfo or error
     */
    suspend fun getCertificateInfo(packageName: String, deviceId: String): Result<CertificateInfo>
}
