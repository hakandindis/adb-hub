package com.github.hakandindis.plugins.adbhub.feature.package_details.data.repository

import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.CertificateDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.PackageDetailsDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.models.CertificateInfo
import com.github.hakandindis.plugins.adbhub.models.PackageDetails
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of PackageDetailsRepository
 */
class PackageDetailsRepositoryImpl(
    private val packageDetailsDataSource: PackageDetailsDataSource,
    private val certificateDataSource: CertificateDataSource
) : PackageDetailsRepository {

    private val logger = Logger.getInstance(PackageDetailsRepositoryImpl::class.java)

    override suspend fun getPackageDetails(packageName: String, deviceId: String): Result<PackageDetails> {
        return try {
            val details = packageDetailsDataSource.getPackageDetails(packageName, deviceId)
            if (details != null) {
                Result.success(details)
            } else {
                Result.failure(Exception("Failed to get package details for $packageName"))
            }
        } catch (e: Exception) {
            logger.error("Error getting package details for $packageName", e)
            Result.failure(e)
        }
    }

    override suspend fun getCertificateInfo(packageName: String, deviceId: String): Result<CertificateInfo> {
        return try {
            val certificateInfo = certificateDataSource.getCertificateInfo(packageName, deviceId)
            if (certificateInfo != null) {
                Result.success(certificateInfo)
            } else {
                Result.failure(Exception("Failed to get certificate info for $packageName"))
            }
        } catch (e: Exception) {
            logger.error("Error getting certificate info for $packageName", e)
            Result.failure(e)
        }
    }
}
