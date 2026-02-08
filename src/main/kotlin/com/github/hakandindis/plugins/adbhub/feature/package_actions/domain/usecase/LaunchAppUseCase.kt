package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

class LaunchAppUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(packageName: String, deviceId: String): Result<Unit> {
        return repository.launchApp(packageName, deviceId)
    }
}
