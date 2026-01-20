package com.github.hakandindis.plugins.adbhub.ui.theme.colors

import androidx.compose.ui.graphics.Color

/**
 * ADB Hub Color definitions
 * Raw color values with color name and tone (e.g., blue500, grey900)
 * These are pure color definitions without semantic meaning.
 * Semantic mapping is done in AdbHubTheme.
 */
object AdbHubColors {
    // Blue colors
    val blue500 = Color(0xFF3574F0)
    val blue600 = Color(0xFF3064CF)

    // Grey colors (from dark to light)
    val grey900 = Color(0xFF1E1F22)  // Darkest - used for background
    val grey800 = Color(0xFF2B2D30)  // Dark - used for surface
    val grey700 = Color(0xFF2D3135)  // Dark grey - used for selection
    val grey600 = Color(0xFF3F4145)  // Medium dark - used for hover
    val grey500 = Color(0xFF4A4D52)  // Medium - used for strong hover
    val grey400 = Color(0xFF43454A)  // Medium grey - used for borders
    val grey300 = Color(0xFF868A91)  // Light grey - used for muted text
    val grey200 = Color(0xFFCED0D6)  // Very light grey - used for main text
    val grey100 = Color(0xFFE5E7EB)  // Almost white
    val grey50 = Color(0xFFF9FAFB)   // Almost white

    // Green colors (success)
    val green500 = Color(0xFF59A869)

    // Terminal/Console colors
    val terminalGreen = Color(0xFF4EC9B0) // Bright green-cyan for terminal commands

    // Red colors (danger/error)
    val red500 = Color(0xFFDB5860)

    // Yellow/Orange colors (warning)
    val yellow500 = Color(0xFFEDA200)

    // White with opacity
    val white5 = Color.White.copy(alpha = 0.05f)
    val white10 = Color.White.copy(alpha = 0.10f)
}
