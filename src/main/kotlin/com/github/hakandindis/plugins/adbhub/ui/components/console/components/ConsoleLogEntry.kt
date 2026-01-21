package com.github.hakandindis.plugins.adbhub.ui.components.console.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.core.models.CommandLog
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ConsoleLogEntry(log: CommandLog) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = log.getFormattedTimestamp(),
                style = JewelTheme.defaultTextStyle.copy(
                    color = AdbHubTheme.consoleTimestamp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.width(70.dp)
            )

            Text(
                text = "\$ ${log.getDisplayCommand()}",
                style = JewelTheme.defaultTextStyle.copy(
                    color = AdbHubTheme.consoleCommand,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.weight(1f)
            )
        }

        if (log.output.isNotBlank()) {
            val outputLines = log.output.lines()
            outputLines.forEach { line ->
                if (line.isNotBlank()) {
                    Text(
                        text = line,
                        style = JewelTheme.defaultTextStyle.copy(
                            color = if (log.isSuccess) {
                                AdbHubTheme.consoleOutput
                            } else {
                                AdbHubTheme.consoleError
                            },
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 78.dp) // Align with command (70dp timestamp + 8dp spacing)
                    )
                }
            }
        }

        if (!log.error.isNullOrBlank()) {
            val errorLines = log.error.lines()
            errorLines.forEach { line ->
                if (line.isNotBlank()) {
                    Text(
                        text = line,
                        style = JewelTheme.defaultTextStyle.copy(
                            color = AdbHubTheme.consoleError,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }

        // Exit code (if failed)
        if (!log.isSuccess) {
            Text(
                text = "Exit code: ${log.exitCode}",
                style = JewelTheme.defaultTextStyle.copy(
                    color = AdbHubTheme.consoleExitCode,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
