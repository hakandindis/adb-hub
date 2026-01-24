package com.github.hakandindis.plugins.adbhub.feature.console_log.presentation

import com.github.hakandindis.plugins.adbhub.core.adb.CommandLogger
import com.intellij.openapi.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

/**
 * ViewModel for Console Log feature (MVI pattern)
 */
class ConsoleLogViewModel(
    private val commandLogger: CommandLogger,
    coroutineScope: CoroutineScope
) : Disposable {

    private val scope = coroutineScope

    private val _uiState = MutableStateFlow(
        ConsoleLogUiState(
            logs = commandLogger.logs.value,
            isEmpty = commandLogger.logs.value.isEmpty()
        )
    )
    val uiState: StateFlow<ConsoleLogUiState> = _uiState.asStateFlow()

    init {
        commandLogger.logs
            .onEach { logs ->
                _uiState.value = ConsoleLogUiState(
                    logs = logs,
                    isEmpty = logs.isEmpty()
                )
            }
            .launchIn(scope)
    }

    /**
     * Handles intents from UI
     */
    fun handleIntent(intent: ConsoleLogIntent) {
        when (intent) {
            is ConsoleLogIntent.ClearLogs -> clearLogs()
        }
    }

    private fun clearLogs() {
        commandLogger.clearLogs()
    }

    override fun dispose() {
        scope.cancel()
    }
}
