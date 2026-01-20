package com.github.hakandindis.plugins.adbhub.ui.theme.shapes

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.dimens.AdbHubDimens

/**
 * ADB Hub Shape definitions
 * Border radius and shape values
 */
object AdbHubShapes {
    // Corner Radius
    val XS = RoundedCornerShape(AdbHubDimens.Radius.XS)
    val SM = RoundedCornerShape(AdbHubDimens.Radius.SM)
    val MD = RoundedCornerShape(AdbHubDimens.Radius.MD)
    val LG = RoundedCornerShape(AdbHubDimens.Radius.LG)
    val XL = RoundedCornerShape(AdbHubDimens.Radius.XL)

    // Custom Shapes
    val RECTANGLE = RoundedCornerShape(0.dp)
    val CIRCLE = CircleShape
}
