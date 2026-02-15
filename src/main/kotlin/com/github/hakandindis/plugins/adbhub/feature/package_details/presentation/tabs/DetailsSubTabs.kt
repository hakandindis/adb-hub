package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.dimens.AdbHubDimens
import com.github.hakandindis.plugins.adbhub.ui.theme.typography.AdbHubTypography
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

private val TabShape = RoundedCornerShape(4.dp)

@Composable
fun DetailsSubTabs(
    selectedTab: DetailsTab,
    onTabSelected: (DetailsTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdbHubTheme.colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT + 16.dp)
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DetailsTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) AdbHubTheme.colors.textPrimary else AdbHubTheme.colors.textMuted
                Box(
                    modifier = Modifier
                        .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT)
                        .clip(TabShape)
                        .then(
                            if (isSelected) {
                                Modifier
                                    .background(AdbHubTheme.colors.tabSelectedBackground)
                                    .border(
                                        width = 1.dp,
                                        color = AdbHubTheme.colors.tabSelectedStroke,
                                        shape = TabShape
                                    )
                            } else {
                                Modifier
                            }
                        )
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        tab.label,
                        style = AdbHubTypography.body,
                        color = tint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdbHubTheme.colors.border)
        )
    }
}
