package com.github.hakandindis.plugins.adbhub.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

/**
 * Reusable component for displaying certificate information rows
 */
@Composable
fun CertificateRow(
    label: String,
    value: String,
    isMonospace: Boolean = false,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(192.dp)
                    .background(AdbHubTheme.surface.copy(alpha = 0.3f))
                    .border(1.dp, AdbHubTheme.border.copy(alpha = 0.5f), RoundedCornerShape(0.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    label,
                    style = JewelTheme.defaultTextStyle
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    value,
                    style = if (isMonospace) JewelTheme.defaultTextStyle else JewelTheme.defaultTextStyle
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
