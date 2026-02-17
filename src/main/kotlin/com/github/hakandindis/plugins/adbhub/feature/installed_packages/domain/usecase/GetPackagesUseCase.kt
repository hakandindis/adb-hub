package com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

class GetPackagesUseCase(
    private val repository: PackageRepository
) {
    suspend operator fun invoke(
        deviceId: String,
        includeSystemApps: Boolean = true
    ): AdbHubResult<List<ApplicationPackage>> =
        repository.getPackages(deviceId, includeSystemApps)
}
