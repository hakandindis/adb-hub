package com.github.hakandindis.plugins.adbhub.core.result

sealed class AdbHubError {

    data class AdbCommandFailed(
        val command: String,
        val exitCode: Int,
        val stderr: String?,
        val stdout: String?
    ) : AdbHubError()

    object AdbNotAvailable : AdbHubError()

    data class DeviceNotFound(val deviceId: String) : AdbHubError()

    data class ParseError(val message: String, val cause: Throwable? = null) : AdbHubError()

    data class NotFound(val message: String) : AdbHubError()

    data class Unknown(val message: String, val cause: Throwable? = null) : AdbHubError()

    fun toUserMessage(): String = when (this) {
        is AdbCommandFailed -> {
            val detail = stderr?.takeIf { it.isNotBlank() } ?: stdout?.takeIf { it.isNotBlank() }
            if (detail != null) {
                "Command failed: ${detail.take(200)}${if (detail.length > 200) "..." else ""}"
            } else {
                "Command failed (exit code: $exitCode)"
            }
        }

        AdbNotAvailable -> "ADB not found or not available"
        is DeviceNotFound -> "Device not found: $deviceId"
        is ParseError -> message
        is NotFound -> message
        is Unknown -> message.ifBlank { cause?.message ?: "An unexpected error occurred" }
    }
}
