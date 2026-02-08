package com.github.hakandindis.plugins.adbhub.core.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Represents a logged ADB command execution
 */
data class CommandLog(
    val id: String,
    val timestamp: LocalDateTime,
    val fullCommand: String,
    val output: String,
    val error: String?,
    val exitCode: Int,
    val isSuccess: Boolean,
    val deviceId: String? = null
) {
    /**
     * Formats the timestamp for display
     */
    fun getFormattedTimestamp(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
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
