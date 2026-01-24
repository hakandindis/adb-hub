package com.github.hakandindis.plugins.adbhub.core.models

/**
 * Result of an ADB command execution
 */
data class CommandResult(
    val command: String,
    val output: String,
    val error: String? = null,
    val exitCode: Int,
    val isSuccess: Boolean = exitCode == 0
)
