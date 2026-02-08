package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.dimens.AdbHubDimens
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey

enum class DetailsTab(val label: String, val icon: IconKey) {
    GeneralInfo("General Info", AdbIcons.info),
    AppLinks("App Links", AdbIcons.link),
    Permissions("Permissions", AdbIcons.settings),
    Activities("Activities", AdbIcons.apps),
    Receivers("Receivers", AdbIcons.link),
    Services("Services", AdbIcons.bolt),
    ContentProviders("Content Providers", AdbIcons.folder)
}

@Composable
fun DetailsSubTabs(
    selectedTab: DetailsTab,
    onTabSelected: (DetailsTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdbHubTheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT)
                .padding(horizontal = 4.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DetailsTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) AdbHubTheme.primary else AdbHubTheme.textMuted
                Column(
                    modifier = Modifier
                        .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp)
                        .width(IntrinsicSize.Max)
                ) {
                    Text(
                        tab.label,
                        style = JewelTheme.defaultTextStyle,
                        color = tint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                if (isSelected) AdbHubTheme.primary
                                else Color.Transparent
                            )
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdbHubTheme.border)
        )
    }
}
