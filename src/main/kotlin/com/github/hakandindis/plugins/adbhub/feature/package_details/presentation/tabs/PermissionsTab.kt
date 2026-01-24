package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.models.PermissionGrantStatus
import com.github.hakandindis.plugins.adbhub.models.PermissionStatus
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@Composable
fun PermissionsTab(
    permissions: List<PermissionStatus>,
    searchText: String,
    onPermissionFilterChange: (String) -> Unit = {}
) {
    val searchState = rememberTextFieldState(searchText)
    LaunchedEffect(searchText) { searchState.setTextAndPlaceCursorAtEnd(searchText) }
    LaunchedEffect(searchState.text.toString()) { onPermissionFilterChange(searchState.text.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextField(
            state = searchState,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Filter permissions...") }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AdbHubShapes.MD)
                .background(AdbHubTheme.surface.copy(alpha = 0.2f))
                .border(1.dp, AdbHubTheme.border, AdbHubShapes.MD)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AdbHubTheme.surface.copy(alpha = 0.4f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Permissions Name",
                        modifier = Modifier.padding(start = 32.dp),
                        style = JewelTheme.defaultTextStyle
                    )
                    Text(
                        "Status",
                        style = JewelTheme.defaultTextStyle
                    )
                }
                LazyColumn {
                    items(permissions) { permission ->
                        PermissionRow(permission)
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionRow(permission: PermissionStatus) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    AdbIcons.settings,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    permission.permission,
                    style = JewelTheme.defaultTextStyle
                )
            }
            StatusBadge(permission.status)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdbHubTheme.border.copy(alpha = 0.5f))
        )
    }
}

@Composable
private fun StatusBadge(status: PermissionGrantStatus) {
    val (color, text) = when (status) {
        PermissionGrantStatus.GRANTED -> AdbHubTheme.success to "Granted"
        PermissionGrantStatus.DENIED -> AdbHubTheme.danger to "Denied"
        PermissionGrantStatus.OPTIONAL -> AdbHubTheme.textMuted to "Optional"
    }
    Box(
        modifier = Modifier
            .clip(AdbHubShapes.XS)
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.2f), AdbHubShapes.XS)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            style = JewelTheme.defaultTextStyle,
            color = color
        )
    }
}
