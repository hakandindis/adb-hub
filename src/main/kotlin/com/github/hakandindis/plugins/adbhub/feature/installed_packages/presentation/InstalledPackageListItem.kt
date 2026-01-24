package com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

@Composable
fun InstalledPackageListItem(
    packageItem: ApplicationPackage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val statusText = when {
        packageItem.isSystemApp -> "System"
        else -> "Installed"
    }

    val packageInitials = packageItem.displayName
        .takeIf { it.length >= 2 }
        ?.take(2)
        ?.uppercase()
        ?: packageItem.packageName
            .takeIf { it.length >= 2 }
            ?.take(2)
            ?.uppercase()
        ?: "AP"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdbHubShapes.SM)
            .then(
                if (isSelected) Modifier.background(AdbHubTheme.primary)
                else Modifier
                    .background(AdbHubTheme.background)
                    .border(1.dp, AdbHubTheme.border.copy(alpha = 0.5f), AdbHubShapes.SM)
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(AdbHubShapes.SM)
                .background(
                    if (isSelected) Color.White.copy(alpha = 0.3f)
                    else AdbHubTheme.itemHover
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    AdbIcons.android,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            } else {
                Text(
                    packageInitials,
                    style = JewelTheme.defaultTextStyle
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                packageItem.packageName,
                style = JewelTheme.defaultTextStyle
            )
            Text(
                statusText,
                style = JewelTheme.defaultTextStyle
            )
        }
    }
}
