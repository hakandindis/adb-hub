package com.github.hakandindis.plugins.adbhub.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.hakandindis.plugins.adbhub.ui.theme.colors.AdbHubColorPalette
import com.github.hakandindis.plugins.adbhub.ui.theme.colors.AdbHubColors
import org.jetbrains.jewel.foundation.theme.JewelTheme

val LocalAdbHubColors = staticCompositionLocalOf<AdbHubColorPalette> {
    error("No AdbHubColors provided. Wrap content in AdbHubTheme { }")
}

@Composable
fun AdbHubTheme(content: @Composable () -> Unit) {
    val isDark = JewelTheme.isDark
    val colors = if (isDark) AdbHubColors.Dark else AdbHubColors.Light
    CompositionLocalProvider(LocalAdbHubColors provides colors, content = content)
}

object AdbHubTheme {
    val colors: AdbHubColorPalette
        @Composable
        get() = LocalAdbHubColors.current
}
