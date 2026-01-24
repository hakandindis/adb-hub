package com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.components.common.ListSection
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icon.IconKey

/**
 * App Actions tab component
 */
@Composable
fun AppActionsTab(
    packageName: String,
    deviceId: String,
    packageActionsViewModel: PackageActionsViewModel,
    suggestedDeepLinks: List<String> = emptyList()
) {
    var deepLinkInput by remember { mutableStateOf("") }
    val deepLinkState = rememberTextFieldState(deepLinkInput)
    LaunchedEffect(deepLinkInput) { deepLinkState.setTextAndPlaceCursorAtEnd(deepLinkInput) }
    LaunchedEffect(deepLinkState.text.toString()) { deepLinkInput = deepLinkState.text.toString() }

    val uiState by packageActionsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ListSection(
            title = "LIFECYCLE",
            icon = AdbIcons.playArrow
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        label = "Launch App",
                        icon = AdbIcons.playArrow,
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.LaunchApp(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isLaunching,
                        modifier = Modifier.weight(1f),
                        color = AdbHubTheme.success
                    )
                    ActionButton(
                        label = "Force Stop",
                        icon = AdbIcons.stopCircle,
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.ForceStop(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isStopping,
                        modifier = Modifier.weight(1f),
                        color = AdbHubTheme.danger
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        label = "Restart",
                        icon = AdbIcons.playArrow,
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.RestartApp(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isRestarting,
                        modifier = Modifier.weight(1f),
                        color = AdbHubTheme.primary
                    )
                    ActionButton(
                        label = "Uninstall",
                        icon = AdbIcons.delete,
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.Uninstall(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isUninstalling,
                        modifier = Modifier.weight(1f),
                        color = AdbHubTheme.textMuted
                    )
                }
            }
        }

        ListSection(
            title = "DATA MANAGEMENT",
            icon = AdbIcons.cleaningServices
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DataActionButton(
                        label = "Clear Data",
                        icon = AdbIcons.cleaningServices,
                        description = "Reset app state",
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.ClearData(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isClearingData,
                        modifier = Modifier.weight(1f)
                    )
                    DataActionButton(
                        label = "Clear Cache",
                        icon = AdbIcons.cleaningServices,
                        description = "Free up space",
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.ClearCache(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isClearingCache,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        ListSection(
            title = "ADVANCED",
            icon = AdbIcons.settings
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AdvancedToggleRow(
                    label = "Stay Awake",
                    description = "Keep screen on while app is visible",
                    icon = AdbIcons.settings,
                    checked = uiState.stayAwakeEnabled,
                    onCheckedChange = { enabled ->
                        packageActionsViewModel.handleIntent(
                            PackageActionsIntent.SetStayAwake(enabled, deviceId)
                        )
                    },
                    isLoading = uiState.isSettingStayAwake
                )
                AdvancedToggleRow(
                    label = "Enable App",
                    description = "Toggle package enabled state",
                    icon = AdbIcons.settings,
                    checked = uiState.packageEnabled,
                    onCheckedChange = { enabled ->
                        packageActionsViewModel.handleIntent(
                            PackageActionsIntent.SetPackageEnabled(packageName, enabled, deviceId)
                        )
                    },
                    isLoading = uiState.isSettingEnabled
                )
            }
        }

        ListSection(
            title = "DEEP LINKS",
            icon = AdbIcons.link
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        state = deepLinkState,
                        modifier = Modifier
                            .weight(1f)
                            .clip(AdbHubShapes.SM)
                            .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM),
                        placeholder = { Text("Enter URL or Deep Link") }
                    )
                    Box(
                        modifier = Modifier
                            .clip(AdbHubShapes.SM)
                            .background(AdbHubTheme.primary)
                            .clickable {
                                val link = deepLinkState.text.toString()
                                if (link.isNotBlank()) {
                                    packageActionsViewModel.handleIntent(
                                        PackageActionsIntent.LaunchDeepLink(link, deviceId)
                                    )
                                    deepLinkState.setTextAndPlaceCursorAtEnd("")
                                    deepLinkInput = ""
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .padding(start = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(AdbIcons.bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text("Trigger", style = JewelTheme.defaultTextStyle)
                        }
                    }
                }
                if (suggestedDeepLinks.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestedDeepLinks.forEach { link ->
                            DeepLinkChip(
                                link = link,
                                onClick = {
                                    packageActionsViewModel.handleIntent(
                                        PackageActionsIntent.LaunchDeepLink(link, deviceId)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    icon: IconKey,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = AdbHubTheme.primary
) {
    Box(
        modifier = modifier
            .clip(AdbHubShapes.MD)
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), AdbHubShapes.MD)
            .clickable(enabled = !isLoading) { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = color)
            Text(label, style = JewelTheme.defaultTextStyle, color = color)
        }
    }
}

@Composable
private fun DataActionButton(
    label: String,
    icon: IconKey,
    description: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(AdbHubShapes.MD)
            .background(AdbHubTheme.surface.copy(alpha = 0.3f))
            .border(1.dp, AdbHubTheme.border, AdbHubShapes.MD)
            .clickable(enabled = !isLoading) { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Text(label, style = JewelTheme.defaultTextStyle)
            Text(
                description,
                style = JewelTheme.defaultTextStyle.copy(fontSize = JewelTheme.defaultTextStyle.fontSize * 0.85f)
            )
        }
    }
}

@Composable
private fun AdvancedToggleRow(
    label: String,
    description: String,
    icon: IconKey,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Column {
                Text(label, style = JewelTheme.defaultTextStyle)
                Text(
                    description,
                    style = JewelTheme.defaultTextStyle.copy(
                        fontSize = JewelTheme.defaultTextStyle.fontSize * 0.85f
                    )
                )
            }
        }
        CustomToggleSwitch(
            checked = checked,
            onCheckedChange = { if (!isLoading) onCheckedChange(it) },
            enabled = !isLoading
        )
    }
}

@Composable
private fun CustomToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 10.dp else (-10).dp,
        animationSpec = tween(durationMillis = 200),
        label = "toggle_thumb"
    )

    Box(
        modifier = Modifier
            .width(44.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (checked && enabled) AdbHubTheme.primary
                else if (!enabled) AdbHubTheme.surface.copy(alpha = 0.5f)
                else AdbHubTheme.surface.copy(alpha = 0.6f)
            )
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = thumbOffset)
                .clip(CircleShape)
                .background(
                    if (enabled) AdbHubTheme.textMain
                    else AdbHubTheme.textMuted
                )
        )
    }
}

@Composable
private fun DeepLinkChip(
    link: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(AdbHubShapes.SM)
            .background(AdbHubTheme.surface.copy(alpha = 0.3f))
            .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(link, style = JewelTheme.defaultTextStyle)
    }
}
