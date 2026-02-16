package com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageHeader
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.components.common.ListSection
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import com.github.hakandindis.plugins.adbhub.ui.theme.typography.AdbHubTypography
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icon.IconKey

@Composable
fun AppActionsTab(
    packageName: String,
    deviceId: String,
    packageActionsViewModel: PackageActionsViewModel,
    uid: String? = null,
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
        PackageHeader(
            packageName = packageName,
            uid = uid,
        )

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
                        color = AdbHubTheme.colors.success
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
                        color = AdbHubTheme.colors.danger
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        label = "Clear Data",
                        icon = AdbIcons.delete,
                        onClick = {
                            packageActionsViewModel.handleIntent(
                                PackageActionsIntent.ClearData(packageName, deviceId)
                            )
                        },
                        isLoading = uiState.isClearingData,
                        modifier = Modifier.weight(1f),
                        color = AdbHubTheme.colors.warning
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
                        color = AdbHubTheme.colors.textMuted
                    )
                }
            }
        }

        ListSection(
            title = "DATA & STORAGE",
            icon = AdbIcons.cleaningServices
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    label = "Clear Cache",
                    icon = AdbIcons.cleaningServices,
                    onClick = {
                        packageActionsViewModel.handleIntent(
                            PackageActionsIntent.ClearCache(packageName, deviceId)
                        )
                    },
                    isLoading = uiState.isClearingCache,
                    modifier = Modifier.fillMaxWidth(),
                    color = AdbHubTheme.colors.primary
                )
            }
        }

        ListSection(
            title = "DEEP LINK",
            icon = AdbIcons.link
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        state = deepLinkState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(AdbHubShapes.SM)
                            .border(1.dp, AdbHubTheme.colors.border, AdbHubShapes.SM),
                        placeholder = { Text("scheme://host/path?query=...") }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(AdbHubShapes.SM)
                            .background(AdbHubTheme.colors.success)
                            .clickable {
                                val uri = deepLinkState.text.toString()
                                if (uri.isNotBlank()) {
                                    packageActionsViewModel.handleIntent(
                                        PackageActionsIntent.LaunchDeepLink(
                                            uri = uri,
                                            deviceId = deviceId
                                        )
                                    )
                                    deepLinkState.setTextAndPlaceCursorAtEnd("")
                                    deepLinkInput = ""
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                AdbIcons.arrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Text("Send", style = AdbHubTypography.body, color = Color.White)
                        }
                    }
                }
                Text(
                    "Triggers an implicit intent with ACTION_VIEW. System resolves which app handles the link.",
                    style = AdbHubTypography.caption,
                    color = AdbHubTheme.colors.textMuted
                )
                if (uiState.recentUris.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Recent Deep Links",
                        style = AdbHubTypography.caption,
                        color = AdbHubTheme.colors.textMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        uiState.recentUris.forEach { uri ->
                            RecentDeepLinkChip(
                                uri = uri,
                                onClick = { deepLinkInput = uri }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentDeepLinkChip(
    uri: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdbHubShapes.SM)
            .background(AdbHubTheme.colors.surface.copy(alpha = 0.5f))
            .border(1.dp, AdbHubTheme.colors.border.copy(alpha = 0.5f), AdbHubShapes.SM)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            uri,
            style = AdbHubTypography.caption,
            maxLines = 2
        )
    }
}

@Composable
private fun ActionButton(
    label: String,
    icon: IconKey,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = AdbHubTheme.colors.primary
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
            Text(label, style = AdbHubTypography.body, color = color)
        }
    }
}
