package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

class ClearDataUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(packageName: String, deviceId: String): Result<Unit> {
        return repository.clearData(packageName, deviceId)
    }
}
