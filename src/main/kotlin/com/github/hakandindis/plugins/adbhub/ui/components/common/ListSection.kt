package com.github.hakandindis.plugins.adbhub.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IntelliJIconKey

/**
 * Reusable section container with title and icon
 */
@Composable
fun ListSection(
    title: String,
    icon: IntelliJIconKey,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(
                title,
                style = JewelTheme.defaultTextStyle
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AdbHubShapes.MD)
                .background(AdbHubTheme.surface.copy(alpha = 0.2f))
                .border(1.dp, AdbHubTheme.border, AdbHubShapes.MD)
        ) {
            Column {
                content()
            }
        }
    }
}
