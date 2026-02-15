package com.github.hakandindis.plugins.adbhub.ui.theme.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.jewel.foundation.theme.JewelTheme

object AdbHubTypography {

    val headline: TextStyle
        @Composable
        get() = JewelTheme.defaultTextStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = JewelTheme.defaultTextStyle.fontSize * 1.25f,
        )

    val body: TextStyle
        @Composable
        get() = JewelTheme.defaultTextStyle


    val caption: TextStyle
        @Composable
        get() = JewelTheme.defaultTextStyle.copy(
            fontSize = JewelTheme.defaultTextStyle.fontSize * 0.875f,
        )

    val label: TextStyle
        @Composable
        get() = JewelTheme.defaultTextStyle.copy(
            fontWeight = FontWeight.Medium,
            fontSize = JewelTheme.defaultTextStyle.fontSize * 0.75f,
        )

    val console: TextStyle
        @Composable
        get() = JewelTheme.consoleTextStyle
}
