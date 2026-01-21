package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

@Composable
fun PackageListItem(
    packageItem: ApplicationPackage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isSelected) AdbIcons.android else AdbIcons.apps,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    packageItem.displayName,
                    style = JewelTheme.defaultTextStyle
                )
                Text(
                    packageItem.packageName,
                    style = JewelTheme.defaultTextStyle
                )
            }
        }
        if (isSelected) {
            Icon(AdbIcons.arrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}
