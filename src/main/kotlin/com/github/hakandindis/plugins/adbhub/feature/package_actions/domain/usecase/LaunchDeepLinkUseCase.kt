package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

/**
 * Use case for launching a deep link
 */
class LaunchDeepLinkUseCase(
    private val repository: PackageActionsRepository
) {
    suspend operator fun invoke(deepLink: String, deviceId: String): Result<Unit> {
        return repository.launchDeepLink(deepLink, deviceId)
    }
}
