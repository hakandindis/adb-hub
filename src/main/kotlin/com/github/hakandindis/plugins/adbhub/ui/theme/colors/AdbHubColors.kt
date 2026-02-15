package com.github.hakandindis.plugins.adbhub.ui.theme.colors

import androidx.compose.ui.graphics.Color

/**
 * Raw color tokens – shared across light and dark palettes.
 * Semantic colors (primary, success, danger) work in both themes.
 */
object AdbHubColorTokens {
    val blue500 = Color(0xFF3574F0)
    val blue600 = Color(0xFF3064CF)

    val green500 = Color(0xFF59A869)
    val terminalGreen = Color(0xFF4EC9B0)
    val red500 = Color(0xFFDB5860)
    val yellow500 = Color(0xFFEDA200)

    // Dark theme greys
    val grey900 = Color(0xFF1E1F22)
    val grey800 = Color(0xFF2B2D30)
    val grey700 = Color(0xFF2D3135)
    val grey600 = Color(0xFF3F4145)
    val grey500 = Color(0xFF4A4D52)
    val grey400 = Color(0xFF43454A)
    val grey300 = Color(0xFF868A91)
    val grey200 = Color(0xFFCED0D6)
    val grey100 = Color(0xFFE5E7EB)
    val grey50 = Color(0xFFF9FAFB)
}

/**
 * Semantic color palette – theme-aware.
 * Use via [AdbHubTheme.colors] inside [AdbHubTheme] composable.
 */
data class AdbHubColorPalette(
    val primary: Color,
    val primaryHover: Color,
    val background: Color,
    val surface: Color,
    val tabSelectedBackground: Color,
    val tabSelectedStroke: Color,
    val border: Color,
    val textPrimary: Color,
    val textMain: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val selection: Color,
    val itemHover: Color,
    val itemHoverStrong: Color,
    val success: Color,
    val danger: Color,
    val warning: Color,
    val consoleCommand: Color,
    val consoleTimestamp: Color,
    val consoleOutput: Color,
    val consoleError: Color,
    val consoleExitCode: Color,
    val successTint: Color,
    val whiteOverlay5: Color,
    val whiteOverlay10: Color,
)

object AdbHubColors {
    private val T = AdbHubColorTokens

    val Dark = AdbHubColorPalette(
        primary = T.blue500,
        primaryHover = T.blue600,
        background = T.grey900,
        surface = T.grey800,
        tabSelectedBackground = T.blue500.copy(alpha = 0.2f),
        tabSelectedStroke = T.blue500.copy(alpha = 0.7f),
        border = T.grey400,
        textPrimary = T.grey200,
        textMain = T.grey200,
        textSecondary = T.grey300,
        textMuted = T.grey300,
        selection = T.grey700,
        itemHover = T.grey600,
        itemHoverStrong = T.grey500,
        success = T.green500,
        danger = T.red500,
        warning = T.yellow500,
        consoleCommand = T.terminalGreen,
        consoleTimestamp = T.grey400,
        consoleOutput = T.grey200,
        consoleError = T.red500,
        consoleExitCode = T.yellow500,
        successTint = T.green500.copy(alpha = 0.1f),
        whiteOverlay5 = Color.White.copy(alpha = 0.05f),
        whiteOverlay10 = Color.White.copy(alpha = 0.10f),
    )

    val Light = AdbHubColorPalette(
        primary = T.blue500,
        primaryHover = T.blue600,
        background = T.grey50,
        surface = Color.White,
        tabSelectedBackground = T.blue500.copy(alpha = 0.12f),
        tabSelectedStroke = T.blue500.copy(alpha = 0.6f),
        border = T.grey100,
        textPrimary = T.grey900,
        textMain = T.grey900,
        textSecondary = T.grey700,
        textMuted = T.grey500,
        selection = T.blue500.copy(alpha = 0.15f),
        itemHover = T.grey100,
        itemHoverStrong = T.grey200,
        success = T.green500,
        danger = T.red500,
        warning = T.yellow500,
        consoleCommand = Color(0xFF0D7D6B),
        consoleTimestamp = T.grey500,
        consoleOutput = T.grey800,
        consoleError = T.red500,
        consoleExitCode = Color(0xFFB45309),
        successTint = T.green500.copy(alpha = 0.12f),
        whiteOverlay5 = Color.Black.copy(alpha = 0.03f),
        whiteOverlay10 = Color.Black.copy(alpha = 0.06f),
    )
}
