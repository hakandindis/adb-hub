package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ActivityUiModel
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@Composable
fun ActivitiesTab(
    activities: List<ActivityUiModel>,
    searchText: String,
    onActivitySearchChange: (String) -> Unit = {},
    onActivityLaunch: (String) -> Unit = {}
) {
    val searchState = rememberTextFieldState(searchText)
    LaunchedEffect(searchText) { searchState.setTextAndPlaceCursorAtEnd(searchText) }
    LaunchedEffect(searchState.text.toString()) { onActivitySearchChange(searchState.text.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AdbHubTheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    AdbIcons.search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                )
                TextField(
                    state = searchState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 96.dp),
                    placeholder = { Text("Search activities (e.g. MainActivity)") }
                )
                Box(
                    modifier = Modifier
                        .clip(AdbHubShapes.XS)
                        .background(AdbHubTheme.surface)
                        .border(1.dp, AdbHubTheme.border, AdbHubShapes.XS)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        "${activities.size} Activities",
                        style = JewelTheme.defaultTextStyle
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(activities) { activity ->
                ActivityRow(
                    activity = activity,
                    onLaunch = {
                        if (activity.isLauncher) {
                            onActivityLaunch(activity.fullName)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ActivityRow(
    activity: ActivityUiModel,
    onLaunch: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdbHubShapes.SM)
            .then(
                if (isHovered)
                    Modifier
                        .background(AdbHubTheme.surface)
                        .border(1.dp, AdbHubTheme.border, AdbHubShapes.SM)
                else
                    Modifier
                        .border(
                            1.dp,
                            Color.Transparent,
                            AdbHubShapes.SM
                        )
            )
            .clickable(interactionSource = interactionSource, indication = null) { }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(AdbHubShapes.SM)
                    .background(
                        if (activity.isLauncher) AdbHubTheme.primary.copy(alpha = 0.1f)
                        else AdbHubTheme.whiteOverlay5
                    )
                    .border(
                        1.dp,
                        if (activity.isLauncher) AdbHubTheme.primary.copy(alpha = 0.2f)
                        else AdbHubTheme.whiteOverlay10,
                        AdbHubShapes.SM
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    AdbIcons.android,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        ".${activity.shortName}",
                        style = JewelTheme.defaultTextStyle
                    )
                    if (activity.isLauncher) {
                        TagBadge("MAIN", AdbHubTheme.primary)
                        TagBadge("LAUNCHER", AdbHubTheme.textMuted)
                    }
                    if (activity.isExported) {
                        TagBadge("EXPORTED", AdbHubTheme.textMuted)
                    }
                    activity.intentFilters.forEach { filter ->
                        filter.actions.forEach { action ->
                            if (action.contains("VIEW", ignoreCase = true)) {
                                TagBadge("VIEW", Color(0xFF818CF8))
                            }
                        }
                    }
                }
                Text(
                    activity.fullName,
                    style = JewelTheme.defaultTextStyle
                )
            }
        }
        if (activity.isLauncher) {
            Box(
                modifier = Modifier
                    .clip(AdbHubShapes.XS)
                    .background(AdbHubTheme.background)
                    .border(1.dp, AdbHubTheme.border, AdbHubShapes.XS)
                    .clickable { onLaunch() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(AdbIcons.playArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                    Text("Launch", style = JewelTheme.defaultTextStyle)
                }
            }
        }
    }
}

@Composable
private fun TagBadge(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(AdbHubShapes.XS)
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.2f), AdbHubShapes.XS)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text,
            style = JewelTheme.defaultTextStyle,
            color = color
        )
    }
}
