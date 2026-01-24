package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey

enum class DetailsTab(val label: String, val icon: IconKey) {
    GeneralInfo("General Info", AdbIcons.info),
    Permissions("Permissions", AdbIcons.settings),
    Activities("Activities", AdbIcons.apps)
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
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DetailsTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            tab.label,
                            style = JewelTheme.defaultTextStyle,
                            color = if (isSelected) AdbHubTheme.primary else AdbHubTheme.textMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .wrapContentWidth()
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdbHubTheme.border)
        )
    }
}
