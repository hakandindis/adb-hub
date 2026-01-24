package com.github.hakandindis.plugins.adbhub.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.console_log.presentation.ConsoleLogTab
import com.github.hakandindis.plugins.adbhub.feature.console_log.presentation.ConsoleLogViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.AppActionsTab
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.DetailsContent
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun AdbMainContent(
    packageDetailsViewModel: PackageDetailsViewModel,
    packageActionsViewModel: PackageActionsViewModel,
    consoleLogViewModel: ConsoleLogViewModel,
    selectedDevice: Device?,
    selectedPackage: ApplicationPackage?,
    uid: String? = null,
    onCopyPath: (String) -> Unit = {},
    onActivityLaunch: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(MainTab.Details) }
    val packageDetailsUiState by packageDetailsViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        MainTabs(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AdbHubTheme.background)
        ) {
            when (selectedTab) {
                MainTab.Details -> DetailsContent(
                    packageDetailsViewModel = packageDetailsViewModel,
                    uid = uid,
                    onCopyPath = onCopyPath,
                    onActivityLaunch = onActivityLaunch
                )

                MainTab.ConsoleLog -> ConsoleLogTab(
                    consoleLogViewModel = consoleLogViewModel
                )

                MainTab.AppActions -> {
                    val deviceId = selectedDevice?.id ?: ""
                    if (selectedPackage != null && selectedDevice != null) {
                        val suggestedDeepLinks = packageDetailsUiState.activities
                            .flatMap { it.intentFilters }
                            .flatMap { it.data }
                            .distinct()
                            .take(5)

                        AppActionsTab(
                            packageName = selectedPackage.packageName,
                            deviceId = deviceId,
                            packageActionsViewModel = packageActionsViewModel,
                            suggestedDeepLinks = suggestedDeepLinks
                        )
                    } else {
                        TabPlaceholder("App Actions", "Select a package to view actions.")
                    }
                }
            }
        }
    }
}

@Composable
private fun TabPlaceholder(title: String, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = JewelTheme.defaultTextStyle)
            Text(message, style = JewelTheme.defaultTextStyle, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
