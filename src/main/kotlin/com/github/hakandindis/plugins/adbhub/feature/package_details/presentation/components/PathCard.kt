package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun PathCard(
    label: String,
    path: String,
    onCopy: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdbHubShapes.SM)
            .background(AdbHubTheme.surface.copy(alpha = 0.3f))
            .border(1.dp, AdbHubTheme.border.copy(alpha = 0.5f), AdbHubShapes.SM)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AdbHubTheme.whiteOverlay5)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = JewelTheme.defaultTextStyle
            )
            IconButton(onClick = { onCopy(path) }) {
                Icon(AdbIcons.contentCopy, contentDescription = "Copy Path", modifier = Modifier.size(14.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                path,
                style = JewelTheme.defaultTextStyle
            )
        }
    }
}
