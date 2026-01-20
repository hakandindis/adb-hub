package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

@Composable
fun PackageHeader(
    packageName: String,
    appName: String,
    versionName: String? = null,
    uid: String? = null,
    targetSdkVersion: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdbHubTheme.surface.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(AdbHubShapes.XL)
                    .background(AdbHubTheme.primary.copy(alpha = 0.2f))
                    .border(1.dp, AdbHubTheme.whiteOverlay10, AdbHubShapes.XL),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    AdbIcons.android,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    appName,
                    style = JewelTheme.defaultTextStyle
                )
                Text(
                    packageName,
                    style = JewelTheme.defaultTextStyle
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge("Installed", AdbHubTheme.success, true)
                    uid?.let {
                        InfoBadge("UID: $it")
                    }
                    targetSdkVersion?.let {
                        InfoBadge("Target SDK: $it")
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdbHubTheme.border)
        )
    }
}

@Composable
private fun StatusBadge(
    text: String,
    color: Color,
    showDot: Boolean = false
) {
    Row(
        modifier = Modifier
            .clip(AdbHubShapes.XS)
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.2f), AdbHubShapes.XS)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (showDot) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Text(
            text,
            style = JewelTheme.defaultTextStyle,
            color = color
        )
    }
}

@Composable
private fun InfoBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(AdbHubShapes.XS)
            .background(AdbHubTheme.whiteOverlay5)
            .border(1.dp, AdbHubTheme.whiteOverlay10, AdbHubShapes.XS)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            style = JewelTheme.defaultTextStyle
        )
    }
}
