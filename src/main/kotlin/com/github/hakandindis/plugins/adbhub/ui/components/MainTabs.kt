package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.dimens.AdbHubDimens
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey

enum class MainTab(val label: String, val icon: IconKey) {
    Details("Details", AdbIcons.info),
    ConsoleLog("Console Log", AdbIcons.terminal),
    AppActions("App Actions", AdbIcons.bolt)
}

@Composable
fun MainTabs(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT)
            .background(AdbHubTheme.surface)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        MainTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Row(
                modifier = Modifier
                    .height(AdbHubDimens.Layout.TAB_BAR_HEIGHT)
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 16.dp)
                    .then(
                        if (isSelected)
                            Modifier.background(AdbHubTheme.selection)
                        else
                            Modifier
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    tab.icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    tab.label,
                    style = JewelTheme.defaultTextStyle
                )
            }
        }
    }
}
