package com.github.hakandindis.plugins.adbhub.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.hakandindis.plugins.adbhub.ui.theme.colors.AdbHubColors

object AdbHubTheme {
    val primary = AdbHubColors.blue500
    val primaryHover = AdbHubColors.blue600

    val background = AdbHubColors.grey900
    val backgroundDark = AdbHubColors.grey900
    val surface = AdbHubColors.grey800
    val surfaceDark = AdbHubColors.grey800

    val border = AdbHubColors.grey400
    val borderDark = AdbHubColors.grey400

    val textPrimary = AdbHubColors.grey200
    val textMain = AdbHubColors.grey200
    val textSecondary = AdbHubColors.grey300
    val textMuted = AdbHubColors.grey300

    val selection = AdbHubColors.grey700
    val itemHover = AdbHubColors.grey600
    val itemHoverStrong = AdbHubColors.grey500

    val success = AdbHubColors.green500
    val danger = AdbHubColors.red500
    val warning = AdbHubColors.yellow500

    val consoleCommand = AdbHubColors.terminalGreen
    val consoleTimestamp = AdbHubColors.grey400
    val consoleOutput = AdbHubColors.grey200
    val consoleError = AdbHubColors.red500
    val consoleExitCode = AdbHubColors.yellow500

    val successTint = success.copy(alpha = 0.1f)
    val whiteOverlay5 = AdbHubColors.white5
    val whiteOverlay10 = AdbHubColors.white10

    @Composable
    fun primaryColor(): Color = primary

    @Composable
    fun surfaceColor(): Color = surface

    @Composable
    fun textMainColor(): Color = textMain

    @Composable
    fun textMutedColor(): Color = textMuted
}
