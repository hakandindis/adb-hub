package com.github.hakandindis.plugins.adbhub.feature.packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Use case for getting list of installed packages
 */
class GetPackagesUseCase(
    private val repository: PackageRepository
) {
    /**
     * Executes the use case
     * @param deviceId Device ID (serial number)
     * @param includeSystemApps Whether to include system apps
     * @return Result containing list of packages or error
     */
    suspend operator fun invoke(deviceId: String, includeSystemApps: Boolean = true): Result<List<ApplicationPackage>> {
        return repository.getPackages(deviceId, includeSystemApps)
    }
}
