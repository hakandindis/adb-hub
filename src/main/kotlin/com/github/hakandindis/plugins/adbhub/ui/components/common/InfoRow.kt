package com.github.hakandindis.plugins.adbhub.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun InfoRow(
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(AdbHubTheme.surface.copy(alpha = 0.3f))
                    .padding(12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    label,
                    style = JewelTheme.defaultTextStyle
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(AdbHubTheme.border.copy(alpha = 0.5f))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    value,
                    style = JewelTheme.defaultTextStyle
                )
            }
        }
        if (!isLast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AdbHubTheme.border.copy(alpha = 0.5f))
            )
        }
    }
}
