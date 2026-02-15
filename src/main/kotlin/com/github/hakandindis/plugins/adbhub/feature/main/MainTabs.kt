package com.github.hakandindis.plugins.adbhub.feature.main

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
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey

enum class MainTab(val label: String, val icon: IconKey) {
    Details("Details", AdbIcons.details),
    AppActions("App Actions", AdbIcons.actionsTarget),
    ConsoleLog("Console Log", AdbIcons.consoleLog)
}

private val TabShape = RoundedCornerShape(4.dp)

@Composable
fun MainTabs(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
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
            MainTab.entries.forEach { tab ->
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            tab.icon,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = tint
                        )
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
        }
    }
}
