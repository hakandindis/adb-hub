package com.github.hakandindis.plugins.adbhub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

/**
 * Shown when ADB is not available (executor is null after initialization).
 * Plugin window opens, initialization runs, and this is displayed when
 * viewModels cannot be created due to missing ADB.
 */
@Composable
fun AdbUnavailableContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdbHubTheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "ADB not available",
            style = JewelTheme.defaultTextStyle
        )
        Text(
            "Install ADB and add it to your PATH, or configure the ADB path in settings.",
            style = JewelTheme.defaultTextStyle.copy(
                fontSize = JewelTheme.defaultTextStyle.fontSize * 0.9f
            ),
            modifier = Modifier.padding(top = 8.dp),
            color = AdbHubTheme.textMuted
        )
    }
}
