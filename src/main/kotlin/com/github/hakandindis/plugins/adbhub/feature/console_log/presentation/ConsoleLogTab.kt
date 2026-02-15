package com.github.hakandindis.plugins.adbhub.feature.console_log.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.typography.AdbHubTypography
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ConsoleLogTab(
    consoleLogViewModel: ConsoleLogViewModel
) {
    val uiState by consoleLogViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdbHubTheme.colors.background)
    ) {
        ConsoleLogToolbar(
            onClearLogs = {
                consoleLogViewModel.handleIntent(ConsoleLogIntent.ClearLogs)
            },
            logCount = uiState.logs.size
        )

        if (uiState.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No commands executed yet",
                    style = AdbHubTypography.body.copy(
                        color = AdbHubTheme.colors.textMuted
                    )
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                uiState.logs.forEach { log ->
                    ConsoleLogEntry(log = log)
                }
            }
        }
    }
}
