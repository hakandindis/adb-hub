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
            // First force stop
            forceStopUseCase(packageName, deviceId).getOrThrow()
            // Then launch
            launchAppUseCase(packageName, deviceId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
