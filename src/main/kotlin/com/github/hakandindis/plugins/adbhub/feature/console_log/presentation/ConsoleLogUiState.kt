package com.github.hakandindis.plugins.adbhub.feature.console_log.presentation

import com.github.hakandindis.plugins.adbhub.core.models.CommandLog

/**
 * UI State for Console Log feature
 */
data class ConsoleLogUiState(
    val logs: List<CommandLog> = emptyList(),
    val isEmpty: Boolean = true
)
