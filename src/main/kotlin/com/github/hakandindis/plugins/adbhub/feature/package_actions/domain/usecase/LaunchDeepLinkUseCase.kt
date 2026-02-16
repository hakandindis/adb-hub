package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

class LaunchDeepLinkUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(uri: String, deviceId: String): Result<Unit> {
        return repository.launchDeepLink(uri, deviceId)
    }
}
