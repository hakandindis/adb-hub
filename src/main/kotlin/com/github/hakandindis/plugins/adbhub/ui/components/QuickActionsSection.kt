package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsIntent
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icon.IconKey

@Composable
fun QuickActionsSection(
    packageId: String,
    versionName: String?,
    deviceId: String,
    packageActionsViewModel: PackageActionsViewModel?,
    onLaunch: () -> Unit = {},
    onForceStop: () -> Unit = {},
    onClearData: () -> Unit = {},
    onUninstall: () -> Unit = {},
    onDeepLink: (String) -> Unit = {}
) {
    var deepLinkInput by remember { mutableStateOf("") }
    val deepLinkState = rememberTextFieldState(deepLinkInput)
    LaunchedEffect(deepLinkInput) { deepLinkState.setTextAndPlaceCursorAtEnd(deepLinkInput) }
    LaunchedEffect(deepLinkState.text.toString()) { deepLinkInput = deepLinkState.text.toString() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AdbHubTheme.primary)
                )
                Text(
                    packageId,
                    modifier = Modifier.padding(start = 8.dp),
                    style = JewelTheme.defaultTextStyle
                )
            }
            Text(
                "v${versionName ?: "—"}",
                style = JewelTheme.defaultTextStyle
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickActionButton(
                label = "Launch App",
                icon = AdbIcons.playArrow,
                onClick = {
                    packageActionsViewModel?.handleIntent(
                        PackageActionsIntent.LaunchApp(packageId, deviceId)
                    ) ?: onLaunch()
                },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Force Stop",
                icon = AdbIcons.stopCircle,
                onClick = {
                    packageActionsViewModel?.handleIntent(
                        PackageActionsIntent.ForceStop(packageId, deviceId)
                    ) ?: onForceStop()
                },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickActionButton(
                label = "Clear Data",
                icon = AdbIcons.cleaningServices,
                onClick = {
                    packageActionsViewModel?.handleIntent(
                        PackageActionsIntent.ClearData(packageId, deviceId)
                    ) ?: onClearData()
                },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Uninstall",
                icon = AdbIcons.delete,
                onClick = {
                    packageActionsViewModel?.handleIntent(
                        PackageActionsIntent.Uninstall(packageId, deviceId)
                    ) ?: onUninstall()
                },
                modifier = Modifier.weight(1f)
            )
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(
                modifier = Modifier.padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(AdbIcons.link, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                Text("DEEP LINK / INTENT", style = JewelTheme.defaultTextStyle)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    state = deepLinkState,
                    modifier = Modifier
                        .weight(1f)
                        .clip(AdbHubShapes.SM)
                        .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM),
                    placeholder = { Text("dilkampi://home") }
                )
                Box(
                    modifier = Modifier
                        .clip(AdbHubShapes.SM)
                        .background(AdbHubTheme.primary)
                        .clickable {
                            val s = deepLinkState.text.toString()
                            if (s.isNotBlank()) {
                                packageActionsViewModel?.handleIntent(
                                    PackageActionsIntent.LaunchDeepLink(s, deviceId)
                                ) ?: onDeepLink(s)
                                deepLinkState.setTextAndPlaceCursorAtEnd("")
                                deepLinkInput = ""
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(AdbIcons.bolt, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("Trigger", style = JewelTheme.defaultTextStyle)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: IconKey,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(AdbHubShapes.MD)
            .background(AdbHubTheme.itemHover)
            .border(1.dp, AdbHubTheme.border, AdbHubShapes.MD)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.padding(bottom = 4.dp))
            Text(label, style = JewelTheme.defaultTextStyle)
        }
    }
}
