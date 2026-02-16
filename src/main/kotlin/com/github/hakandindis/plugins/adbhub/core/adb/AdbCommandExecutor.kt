package com.github.hakandindis.plugins.adbhub.core.adb

import com.github.hakandindis.plugins.adbhub.core.models.CommandResult
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import java.io.File

class AdbCommandExecutor(private val adbPath: String) {

    companion object {
        private const val DEFAULT_TIMEOUT_SECONDS = 30L
    }

    private val commandLogger = CommandLogger.getInstance()

    fun executeCommand(
        command: String,
        timeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS
    ): CommandResult {
        val result = internalExecuteCommand(command, timeoutSeconds)
        commandLogger.logCommand(adbPath, command, result, deviceId = null)

        return result
    }

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

    private fun internalExecuteCommand(
        command: String,
        timeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS
    ): CommandResult {
        val fullCommand = parseCommandLine(command)

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

    private fun parseCommandLine(command: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var quoteChar: Char? = null

        for (c in command) {
            when {
                (c == '"' || c == '\'') && !inQuotes -> {
                    inQuotes = true
                    quoteChar = c
                }

                inQuotes && c == quoteChar -> {
                    inQuotes = false
                    quoteChar = null
                }

                (c == ' ' || c == '\t') && !inQuotes -> {
                    if (current.isNotEmpty()) {
                        result.add(current.toString())
                        current.clear()
                    }
                }

                else -> current.append(c)
            }
        }
        if (current.isNotEmpty()) result.add(current.toString())
        return result
    }
}
