package com.github.hakandindis.plugins.adbhub.core.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Represents a logged ADB command execution
 */
data class CommandLog(
    val id: String,
    val timestamp: LocalDateTime,
    val fullCommand: String, // Full command including "adb" prefix
    val output: String,
    val error: String?,
    val exitCode: Int,
    val isSuccess: Boolean,
    val deviceId: String? = null // Device ID if command was device-specific
) {
    /**
     * Formats the timestamp for display
     */
    fun getFormattedTimestamp(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    /**
     * Gets the display command (without device ID for cleaner display)
     */
    fun getDisplayCommand(): String {
        return if (deviceId != null && fullCommand.startsWith("adb -s $deviceId")) {
            fullCommand.replace("adb -s $deviceId", "adb")
        } else {
            fullCommand
        }
    }

    /**
     * Gets the full output including error if present
     */
    fun getFullOutput(): String {
        return if (error != null && error.isNotBlank()) {
            "$output\n$error"
        } else {
            output
        }
    }
}
