package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

/**
 * Use case for enabling/disabling a package
 */
class SetPackageEnabledUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(packageName: String, enabled: Boolean, deviceId: String): Result<Unit> {
        return repository.setPackageEnabled(packageName, enabled, deviceId)
    }
}
