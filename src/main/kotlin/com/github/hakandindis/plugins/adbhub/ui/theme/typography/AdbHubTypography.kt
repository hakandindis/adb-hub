package com.github.hakandindis.plugins.adbhub.ui.theme.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.typography

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
        get() = JewelTheme.typography.labelTextStyle

    val console: TextStyle
        @Composable
        get() = JewelTheme.typography.consoleTextStyle

    val labelTextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.labelTextStyle

    val labelTextSize: TextUnit
        @Composable
        get() = JewelTheme.typography.labelTextSize

    val h0TextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.h0TextStyle

    val h1TextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.h1TextStyle

    val h2TextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.h2TextStyle

    val h3TextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.h3TextStyle

    val h4TextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.h4TextStyle

    val regular: TextStyle
        @Composable
        get() = JewelTheme.typography.regular

    val medium: TextStyle
        @Composable
        get() = JewelTheme.typography.medium

    val small: TextStyle
        @Composable
        get() = JewelTheme.typography.small

    val editorTextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.editorTextStyle

    val consoleTextStyle: TextStyle
        @Composable
        get() = JewelTheme.typography.consoleTextStyle
}
