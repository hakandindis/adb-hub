package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase

/**
 * Use case for restarting an app (force stop + launch)
 */
class RestartAppUseCase(
    private val forceStopUseCase: ForceStopUseCase,
    private val launchAppUseCase: LaunchAppUseCase
) {
    suspend operator fun invoke(packageName: String, deviceId: String): Result<Unit> {
        return try {
            forceStopUseCase(packageName, deviceId).getOrThrow()
            launchAppUseCase(packageName, deviceId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
