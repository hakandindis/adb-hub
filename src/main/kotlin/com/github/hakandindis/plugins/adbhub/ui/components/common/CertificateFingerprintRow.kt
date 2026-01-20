package com.github.hakandindis.plugins.adbhub.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

/**
 * Reusable component for displaying certificate fingerprint information
 */
@Composable
fun CertificateFingerprintRow(
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AdbHubTheme.surface.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    label,
                    style = JewelTheme.defaultTextStyle
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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
