package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs.*
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun DetailsContent(
    packageDetailsUiState: PackageDetailsUiState?,
    selectedDevice: Device?,
    uid: String? = null,
    onCopyPath: (String) -> Unit = {},
    onActivityLaunch: (String) -> Unit = {}
) {
    if (packageDetailsUiState == null || packageDetailsUiState.generalInfoItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (packageDetailsUiState?.isLoading == true) {
                    "Loading package details..."
                } else {
                    "Select a package to view details"
                },
                style = JewelTheme.defaultTextStyle
            )
        }
        return
    }

    var selectedTab by remember { mutableStateOf(DetailsTab.GeneralInfo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdbHubTheme.background)
    ) {
        val packageName = packageDetailsUiState.generalInfoItems
            .firstOrNull { it.label == "Package" }?.value ?: ""
        val appName = packageDetailsUiState.generalInfoItems
            .firstOrNull { it.label == "App Name" }?.value ?: packageName

        PackageHeader(
            packageName = packageName,
            uid = uid
        )
        DetailsSubTabs(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AdbHubTheme.background)
        ) {
            when (selectedTab) {
                DetailsTab.GeneralInfo -> GeneralInfoTab(
                    generalInfoItems = packageDetailsUiState.generalInfoItems,
                    pathItems = packageDetailsUiState.pathItems,
                    onCopyPath = onCopyPath
                )

                DetailsTab.Permissions -> PermissionsTab(
                    permissions = packageDetailsUiState.permissions.map { it.status }
                )

                DetailsTab.Activities -> ActivitiesTab(
                    activities = packageDetailsUiState.activities,
                    packageName = packageName,
                    selectedDevice = selectedDevice,
                    onActivityLaunch = onActivityLaunch
                )
            }
        }
    }
}
