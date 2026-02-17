package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

class SetPackageEnabledUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(packageName: String, enabled: Boolean, deviceId: String): AdbHubResult<Unit> =
        repository.setPackageEnabled(packageName, enabled, deviceId)
}
