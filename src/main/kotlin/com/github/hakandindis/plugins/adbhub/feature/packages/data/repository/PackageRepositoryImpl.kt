package com.github.hakandindis.plugins.adbhub.feature.packages.data.repository

import com.github.hakandindis.plugins.adbhub.feature.packages.data.datasource.PackageDataSource
import com.github.hakandindis.plugins.adbhub.feature.packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of PackageRepository
 */
class PackageRepositoryImpl(
    private val dataSource: PackageDataSource
) : PackageRepository {

    private val logger = Logger.getInstance(PackageRepositoryImpl::class.java)

    override suspend fun getPackages(deviceId: String, includeSystemApps: Boolean): Result<List<ApplicationPackage>> {
        return try {
            val packages = dataSource.getPackages(deviceId, includeSystemApps)
            Result.success(packages)
        } catch (e: Exception) {
            logger.error("Error getting packages for device $deviceId", e)
            Result.failure(e)
        }
    }
}
