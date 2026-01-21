package com.github.hakandindis.plugins.adbhub.feature.packages.data.datasource

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Data source interface for package-related operations
 */
interface PackageDataSource {
    /**
     * Gets list of installed packages on a device
     * @param deviceId Device ID (serial number)
     * @param includeSystemApps Whether to include system apps
     * @return List of installed packages
     */
    suspend fun getPackages(deviceId: String, includeSystemApps: Boolean = true): List<ApplicationPackage>
}
