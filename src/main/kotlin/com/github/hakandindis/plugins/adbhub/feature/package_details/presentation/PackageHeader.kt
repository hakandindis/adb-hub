package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import com.github.hakandindis.plugins.adbhub.ui.theme.typography.AdbHubTypography
import org.jetbrains.jewel.ui.component.Text

@Composable
fun PackageHeader(
    packageName: String,
    uid: String? = null
) {
    val packageInitials = packageName
        .takeIf { it.length >= 2 }
        ?.take(2)
        ?.uppercase()
        ?: "AP"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdbHubTheme.colors.surface)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(AdbHubShapes.MD)
                    .background(AdbHubTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    packageInitials,
                    style = AdbHubTypography.body,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    packageName,
                    style = AdbHubTypography.body
                )
                uid?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "UID: $it",
                            style = AdbHubTypography.body
                        )
                    }
                }
            }
        }
    }
}

