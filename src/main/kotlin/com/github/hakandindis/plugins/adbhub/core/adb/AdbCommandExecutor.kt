package com.github.hakandindis.plugins.adbhub.core.adb

import com.github.hakandindis.plugins.adbhub.core.models.CommandResult
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import java.io.File

/**
 * Service for executing ADB commands
 */
class AdbCommandExecutor(private val adbPath: String) {

    companion object {
        private const val DEFAULT_TIMEOUT_SECONDS = 30L
    }

    private val commandLogger = CommandLogger.getInstance()

    /**
     * Executes an ADB command
     * @param command ADB command (without 'adb' prefix)
     * @param timeoutSeconds Timeout in seconds
     * @return CommandResult with output and exit code
     */
    fun executeCommand(
        command: String,
        timeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS
    ): CommandResult {
        val result = internalExecuteCommand(command, timeoutSeconds)
        commandLogger.logCommand(adbPath, command, result, deviceId = null)

        return result
    }

    /**
     * Executes an ADB command with device selection
     * @param deviceId Device ID (serial number)
     * @param command ADB command (without 'adb' prefix)
     * @param timeoutSeconds Timeout in seconds
     */
    fun executeCommandForDevice(
        deviceId: String,
        command: String,
        timeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS
    ): CommandResult {
        val deviceCommand = "-s $deviceId $command"
        val result = internalExecuteCommand(deviceCommand, timeoutSeconds)

        commandLogger.logCommand(adbPath, command, result, deviceId = deviceId)

        return result
    }

    /**
     * Internal method to execute command without logging
     */
    private fun internalExecuteCommand(
        command: String,
        timeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS
    ): CommandResult {
        val fullCommand = command.split(" ").filter { it.isNotBlank() }

        val cmdLine = GeneralCommandLine(adbPath)
            .withParameters(fullCommand)
            .withWorkDirectory(File(System.getProperty("user.home")))

        return try {
            val handler = CapturingProcessHandler(cmdLine)
            val output = handler.runProcess((timeoutSeconds * 1000).toInt())

            CommandResult(
                command = command,
                output = output.stdout,
                error = output.stderr.ifBlank { null },
                exitCode = output.exitCode,
                isSuccess = output.exitCode == 0
            )
        } catch (e: Exception) {
            CommandResult(
                command = command,
                output = "",
                error = e.message ?: "Unknown error",
                exitCode = -1,
                isSuccess = false
            )
        }
    }
}
