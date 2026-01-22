package com.github.hakandindis.plugins.adbhub.ui.components.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PathItemUiModel
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.components.common.InfoRow
import com.github.hakandindis.plugins.adbhub.ui.components.common.ListSection
import com.github.hakandindis.plugins.adbhub.ui.components.common.PathCard

@Composable
fun GeneralInfoTab(
    generalInfoItems: List<InfoItemUiModel>,
    pathItems: List<PathItemUiModel>,
    onCopyPath: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 1280.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            GeneralSection(
                generalInfoItems = generalInfoItems,
                modifier = Modifier.weight(1f)
            )
            PathsSection(
                pathItems = pathItems,
                onCopyPath = onCopyPath,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GeneralSection(
    generalInfoItems: List<InfoItemUiModel>,
    modifier: Modifier = Modifier
) {
    ListSection(
        title = "GENERAL",
        icon = AdbIcons.info,
        modifier = modifier
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

@Composable
private fun PathsSection(
    pathItems: List<PathItemUiModel>,
    onCopyPath: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ListSection(
        title = "PATHS",
        icon = AdbIcons.folderOpen,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            pathItems.forEach { pathItem ->
                PathCard(pathItem.label, pathItem.path, onCopyPath)
            }
        }
    }
}

