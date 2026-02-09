package com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

class GetPackagesUseCase(
    private val repository: PackageRepository
) {
    suspend operator fun invoke(deviceId: String, includeSystemApps: Boolean = true): Result<List<ApplicationPackage>> {
        return repository.getPackages(deviceId, includeSystemApps)
    }
}
