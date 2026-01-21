package com.github.hakandindis.plugins.adbhub.core.adb

import com.github.hakandindis.plugins.adbhub.core.models.CommandLog
import com.github.hakandindis.plugins.adbhub.core.models.CommandResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.util.*

/**
 * Service for logging ADB command executions
 * Singleton service that maintains a log of all executed commands
 */
class CommandLogger {
    private val _logs = MutableStateFlow<List<CommandLog>>(emptyList())
    val logs: StateFlow<List<CommandLog>> = _logs.asStateFlow()

    /**
     * Logs a command execution
     */
    fun logCommand(
        adbPath: String,
        command: String,
        result: CommandResult,
        deviceId: String? = null
    ) {
        val fullCommand = if (deviceId != null) {
            "adb -s $deviceId $command"
        } else {
            "adb $command"
        }

        val log = CommandLog(
            id = UUID.randomUUID().toString(),
            timestamp = LocalDateTime.now(),
            fullCommand = fullCommand,
            output = result.output,
            error = result.error,
            exitCode = result.exitCode,
            isSuccess = result.isSuccess,
            deviceId = deviceId
        )

        _logs.update { currentLogs ->
            (currentLogs + log).takeLast(MAX_LOG_ENTRIES) // Keep only last N entries
        }
    }

    /**
     * Clears all logs
     */
    fun clearLogs() {
        _logs.value = emptyList()
    }

    companion object {
        private const val MAX_LOG_ENTRIES = 1000 // Keep last 1000 commands

        @Volatile
        private var INSTANCE: CommandLogger? = null

        fun getInstance(): CommandLogger {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CommandLogger().also { INSTANCE = it }
            }
        }
    }
}
