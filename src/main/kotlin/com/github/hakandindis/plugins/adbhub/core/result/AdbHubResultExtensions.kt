package com.github.hakandindis.plugins.adbhub.core.result

import com.github.hakandindis.plugins.adbhub.core.models.CommandResult

fun <T> Result<T>.toAdbHubResult(): AdbHubResult<T> = fold(
    onSuccess = { AdbHubResult.success(it) },
    onFailure = { AdbHubResult.failure(AdbHubError.Unknown(it.message ?: "Unknown error", it)) }
)

fun CommandResult.toAdbHubResult(): AdbHubResult<Unit> =
    if (isSuccess) {
        AdbHubResult.success(Unit)
    } else {
        AdbHubResult.failure(
            AdbHubError.AdbCommandFailed(
                command = command,
                exitCode = exitCode,
                stderr = error?.takeIf { it.isNotBlank() },
                stdout = output.takeIf { it.isNotBlank() }
            )
        )
    }

fun CommandResult.toAdbHubResultWithOutput(): AdbHubResult<String> =
    if (isSuccess) {
        AdbHubResult.success(output)
    } else {
        AdbHubResult.failure(
            AdbHubError.AdbCommandFailed(
                command = command,
                exitCode = exitCode,
                stderr = error?.takeIf { it.isNotBlank() },
                stdout = output.takeIf { it.isNotBlank() }
            )
        )
    }
