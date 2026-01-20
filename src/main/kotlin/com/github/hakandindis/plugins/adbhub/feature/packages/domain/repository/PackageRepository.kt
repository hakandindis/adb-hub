package com.github.hakandindis.plugins.adbhub.feature.packages.domain.repository

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Repository interface for package operations
 */
interface PackageRepository {
    /**
     * Gets list of installed packages on a device
     * @param deviceId Device ID (serial number)
     * @param includeSystemApps Whether to include system apps
     * @return Result containing list of packages or error
     */
    suspend fun getPackages(deviceId: String, includeSystemApps: Boolean = true): Result<List<ApplicationPackage>>
}
