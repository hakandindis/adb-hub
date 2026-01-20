package com.github.hakandindis.plugins.adbhub.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.hakandindis.plugins.adbhub.ui.theme.colors.AdbHubColors

/**
 * Semantic theme for ADB Hub
 * Maps raw colors (from AdbHubColors) to meaningful semantic names
 * 
 * Note: For dimensions, shapes, and spacing, use AdbHubDimens, AdbHubShapes, 
 * and AdbHubSpacing directly.
 */
object AdbHubTheme {
    // ─── Primary Colors ────────────────────────────────────────────────────────
    val primary = AdbHubColors.blue500
    val primaryHover = AdbHubColors.blue600

    // ─── Background Colors ────────────────────────────────────────────────────
    val background = AdbHubColors.grey900
    val backgroundDark = AdbHubColors.grey900
    val surface = AdbHubColors.grey800
    val surfaceDark = AdbHubColors.grey800

    // ─── Border Colors ──────────────────────────────────────────────────────────
    val border = AdbHubColors.grey400
    val borderDark = AdbHubColors.grey400

    // ─── Text Colors ────────────────────────────────────────────────────────────
    val textPrimary = AdbHubColors.grey200
    val textMain = AdbHubColors.grey200
    val textSecondary = AdbHubColors.grey300
    val textMuted = AdbHubColors.grey300

    // ─── State Colors ──────────────────────────────────────────────────────────
    val selection = AdbHubColors.grey700
    val itemHover = AdbHubColors.grey600
    val itemHoverStrong = AdbHubColors.grey500

    // ─── Semantic Colors ────────────────────────────────────────────────────────
    val success = AdbHubColors.green500
    val danger = AdbHubColors.red500
    val warning = AdbHubColors.yellow500

    // ─── Opacity Helpers ────────────────────────────────────────────────────────
    val successTint = success.copy(alpha = 0.1f)
    val whiteOverlay5 = AdbHubColors.white5
    val whiteOverlay10 = AdbHubColors.white10

    // ─── Composable getters (for future JewelTheme integration) ──────────────
    @Composable
    fun primaryColor(): Color = primary

    @Composable
    fun surfaceColor(): Color = surface

    @Composable
    fun textMainColor(): Color = textMain

    @Composable
    fun textMutedColor(): Color = textMuted
}
