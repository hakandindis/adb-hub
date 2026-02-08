package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@Composable
fun PermissionsTab(
    permissionSections: List<PermissionSectionUiModel>,
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
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Filter permissions...") }
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            for (section in permissionSections) {
                permissionSection(section)
            }
        }
    }
}

private fun LazyListScope.permissionSection(section: PermissionSectionUiModel) {
    item(key = section.sectionType.name) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AdbHubShapes.MD)
                .background(AdbHubTheme.surface.copy(alpha = 0.2f))
                .border(1.dp, AdbHubTheme.border, AdbHubShapes.MD)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AdbHubTheme.surface.copy(alpha = 0.4f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    AdbIcons.settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    section.sectionTitle,
                    style = JewelTheme.defaultTextStyle
                )
            }
            Column {
                section.items.forEachIndexed { index, item ->
                    PermissionRow(item)
                    if (index < section.items.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(AdbHubTheme.border.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionRow(item: PermissionItemUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            item.name,
            style = JewelTheme.defaultTextStyle,
            modifier = Modifier.weight(1f, fill = false)
        )
        when {
            item.permissionType.showsGrantedBadge() && item.detail == "granted=true" ->
                GrantedBadge()

            item.permissionType.showsGrantedBadge() && item.detail == "granted=false" ->
                DeniedBadge()

            item.detail != null -> Text(
                item.detail,
                style = JewelTheme.defaultTextStyle.copy(
                    fontSize = JewelTheme.defaultTextStyle.fontSize * 0.9f
                ),
                color = AdbHubTheme.textMuted
            )
        }
    }
}

@Composable
private fun GrantedBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(AdbHubTheme.consoleCommand),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(12.dp)) {
                val path = Path().apply {
                    moveTo(2f, size.height * 0.55f)
                    lineTo(size.width * 0.28f, size.height * 0.78f)
                    lineTo(size.width * 0.85f, size.height * 0.22f)
                }
                drawPath(path, Color.White, style = Stroke(width = 2f))
            }
        }
        Text(
            "Granted",
            style = JewelTheme.defaultTextStyle.copy(
                fontSize = JewelTheme.defaultTextStyle.fontSize * 0.9f
            ),
            color = AdbHubTheme.consoleCommand
        )
    }
}

@Composable
private fun DeniedBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(AdbHubTheme.danger),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(12.dp)) {
                drawLine(
                    color = Color.White,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }
        }
        Text(
            "Denied",
            style = JewelTheme.defaultTextStyle.copy(
                fontSize = JewelTheme.defaultTextStyle.fontSize * 0.9f
            ),
            color = AdbHubTheme.danger
        )
    }
}
