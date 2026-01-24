package com.github.hakandindis.plugins.adbhub.feature.console_log.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ConsoleLogToolbar(
    onClearLogs: () -> Unit,
    logCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdbHubTheme.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$logCount command${if (logCount != 1) "s" else ""} logged",
            style = JewelTheme.defaultTextStyle.copy(
                color = AdbHubTheme.textMuted
            )
        )

        Box(
            modifier = Modifier
                .height(28.dp)
                .clip(AdbHubShapes.SM)
                .background(AdbHubTheme.itemHover)
                .border(1.dp, AdbHubTheme.border.copy(alpha = 0.3f), AdbHubShapes.SM)
                .clickable { onClearLogs() }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    key = AdbIcons.cleaningServices,
                    contentDescription = "Clear",
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    "Clear",
                    style = JewelTheme.defaultTextStyle
                )
            }
        }
    }
}
