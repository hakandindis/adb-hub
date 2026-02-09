package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

class SetStayAwakeUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(enabled: Boolean, deviceId: String): Result<Unit> {
        return repository.setStayAwake(enabled, deviceId)
    }
}
