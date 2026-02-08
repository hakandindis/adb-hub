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
    onActivitySearchChange: (String) -> Unit = {}
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                TextField(
                    state = searchState,
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text("Search activities (e.g. MainActivity)") }
                )

                Spacer(Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .clip(AdbHubShapes.XS)
                        .background(AdbHubTheme.surface)
                        .border(1.dp, AdbHubTheme.border, AdbHubShapes.XS)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .padding(end = 8.dp)
                        .wrapContentWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        "${activities.size} Activities",
                        style = JewelTheme.defaultTextStyle,
                        modifier = Modifier.align(Alignment.Center)
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
                ActivityRow(activity = activity)
            }
        }
    }
}

@Composable
private fun ActivityRow(activity: ActivityUiModel) {
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
                    Modifier.border(1.dp, Color.Transparent, AdbHubShapes.SM)
            )
            .clickable(interactionSource = interactionSource, indication = null) { }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(AdbHubShapes.SM)
                .background(AdbHubTheme.whiteOverlay5)
                .border(1.dp, AdbHubTheme.whiteOverlay10, AdbHubShapes.SM),
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
            Text(
                ".${activity.shortName}",
                style = JewelTheme.defaultTextStyle
            )
            Text(
                activity.fullName,
                style = JewelTheme.defaultTextStyle
            )
        }
    }
}
