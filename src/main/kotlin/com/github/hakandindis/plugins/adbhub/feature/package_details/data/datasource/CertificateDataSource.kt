package com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource

import com.github.hakandindis.plugins.adbhub.models.CertificateInfo

/**
 * Data source interface for certificate information operations
 */
interface CertificateDataSource {
    /**
     * Gets certificate information for a package
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return CertificateInfo with certificate details
     */
    suspend fun getCertificateInfo(packageName: String, deviceId: String): CertificateInfo?
}
