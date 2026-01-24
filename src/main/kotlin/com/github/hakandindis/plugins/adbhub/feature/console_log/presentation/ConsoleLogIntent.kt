package com.github.hakandindis.plugins.adbhub.feature.console_log.presentation

/**
 * Intent actions for Console Log feature (MVI pattern)
 */
sealed class ConsoleLogIntent {
    object ClearLogs : ConsoleLogIntent()
}
