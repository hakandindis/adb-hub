package com.github.hakandindis.plugins.adbhub.feature.main

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
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey

enum class MainTab(val label: String, val icon: IconKey) {
    Details("Details", AdbIcons.details),
    AppActions("App Actions", AdbIcons.actionsTarget),
    ConsoleLog("Console Log", AdbIcons.consoleLog)
}

@Composable
fun MainTabs(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
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
            MainTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) AdbHubTheme.primary else AdbHubTheme.textMuted
                Column(
                    modifier = Modifier
                        .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp)
                        .width(IntrinsicSize.Max)
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
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
                            style = JewelTheme.defaultTextStyle,
                            color = tint,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
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
    }
}
