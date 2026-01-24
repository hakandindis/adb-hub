package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.components.InfoRow
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes

@Composable
fun GeneralInfoTab(
    generalInfoItems: List<InfoItemUiModel>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AdbHubShapes.MD)
                .background(AdbHubTheme.surface.copy(alpha = 0.2f))
                .border(1.dp, AdbHubTheme.border, AdbHubShapes.MD)
        ) {
            LazyColumn {
                itemsIndexed(generalInfoItems) { index, item ->
                    InfoRow(
                        label = item.label,
                        value = item.value,
                        isLast = index == generalInfoItems.size - 1
                    )
                }
            }
        }
    }
}

