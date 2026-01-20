package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.dimens.AdbHubDimens
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun AdbToolbar(
    isAdbConnected: Boolean,
    onSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AdbHubDimens.Layout.HEADER_HEIGHT)
            .background(AdbHubTheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AdbHubDimens.Layout.HEADER_HEIGHT)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    AdbIcons.plugConnect,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "ADB Workflow",
                    modifier = Modifier.padding(start = 12.dp),
                    style = JewelTheme.defaultTextStyle
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(AdbHubShapes.XS)
                        .background(AdbHubTheme.whiteOverlay5)
                        .border(1.dp, AdbHubTheme.whiteOverlay10, AdbHubShapes.XS)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isAdbConnected) AdbHubTheme.success else AdbHubTheme.textMuted)
                            .padding(end = 6.dp)
                    )
                    Text(
                        if (isAdbConnected) "ADB Connected" else "ADB Disconnected",
                        style = JewelTheme.defaultTextStyle
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(AdbIcons.settings, contentDescription = "Settings", modifier = Modifier.size(18.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdbHubTheme.border)
                .align(Alignment.BottomCenter)
        )
    }
}
