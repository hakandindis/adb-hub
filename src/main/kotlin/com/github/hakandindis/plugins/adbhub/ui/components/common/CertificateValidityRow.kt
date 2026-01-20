package com.github.hakandindis.plugins.adbhub.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

/**
 * Reusable component for displaying certificate validity information
 */
@Composable
fun CertificateValidityRow(
    from: String,
    to: String,
    isValid: Boolean,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(192.dp)
                    .background(AdbHubTheme.surface.copy(alpha = 0.3f))
                    .border(1.dp, AdbHubTheme.border.copy(alpha = 0.5f), RoundedCornerShape(0.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    "Validity",
                    style = JewelTheme.defaultTextStyle
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("From", style = JewelTheme.defaultTextStyle)
                    Text(from, style = JewelTheme.defaultTextStyle)
                    Text("→", style = JewelTheme.defaultTextStyle)
                    Text("To", style = JewelTheme.defaultTextStyle)
                    Text(to, style = JewelTheme.defaultTextStyle)
                    if (isValid) {
                        Box(
                            modifier = Modifier
                                .clip(AdbHubShapes.XS)
                                .background(AdbHubTheme.success.copy(alpha = 0.1f))
                                .border(
                                    1.dp,
                                    AdbHubTheme.success.copy(alpha = 0.2f),
                                    AdbHubShapes.XS
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "Valid",
                                style = JewelTheme.defaultTextStyle,
                                color = AdbHubTheme.success
                            )
                        }
                    }
                }
            }
        }
        if (!isLast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AdbHubTheme.border.copy(alpha = 0.5f))
            )
        }
    }
}
