package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

/**
 * Use case for clearing app cache
 */
class ClearCacheUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(packageName: String, deviceId: String): Result<Unit> {
        return repository.clearCache(packageName, deviceId)
    }
}
