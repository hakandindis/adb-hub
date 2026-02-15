package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs.*
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.typography.AdbHubTypography
import org.jetbrains.jewel.ui.component.Text

@Composable
fun DetailsContent(
    packageDetailsViewModel: PackageDetailsViewModel,
    uid: String? = null
) {
    val packageDetailsUiState by packageDetailsViewModel.uiState.collectAsState()

    if (packageDetailsUiState.generalInfoItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (packageDetailsUiState.isLoading) {
                    "Loading package details..."
                } else {
                    "Select a package to view details"
                },
                style = AdbHubTypography.body
            )
        }
        return
    }

    var selectedTab by remember { mutableStateOf(DetailsTab.GeneralInfo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdbHubTheme.colors.background)
    ) {
        PackageHeader(
            packageName = packageDetailsUiState.packageName,
            uid = uid
        )

        DetailsSubTabs(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AdbHubTheme.colors.background)
        ) {
            when (selectedTab) {
                DetailsTab.GeneralInfo -> GeneralInfoTab(
                    generalInfoItems = packageDetailsUiState.generalInfoItems
                )

                DetailsTab.Permissions -> PermissionsTab(
                    permissionSections = packageDetailsUiState.filteredPermissionSections,
                    searchText = packageDetailsUiState.permissionSearchText,
                    onPermissionFilterChange = {
                        packageDetailsViewModel.handleIntent(
                            PackageDetailsIntent.FilterPermissions(it)
                        )
                    }
                )

                DetailsTab.Activities -> ActivitiesTab(
                    activities = packageDetailsUiState.filteredActivities,
                    searchText = packageDetailsUiState.activitySearchText,
                    onActivitySearchChange = {
                        packageDetailsViewModel.handleIntent(
                            PackageDetailsIntent.FilterActivities(it)
                        )
                    }
                )

                DetailsTab.Receivers -> ReceiversTab(
                    receivers = packageDetailsUiState.filteredReceivers,
                    searchText = packageDetailsUiState.receiverSearchText,
                    onReceiverSearchChange = {
                        packageDetailsViewModel.handleIntent(
                            PackageDetailsIntent.FilterReceivers(it)
                        )
                    }
                )

                DetailsTab.Services -> ServicesTab(
                    services = packageDetailsUiState.filteredServices,
                    searchText = packageDetailsUiState.serviceSearchText,
                    onServiceSearchChange = {
                        packageDetailsViewModel.handleIntent(
                            PackageDetailsIntent.FilterServices(it)
                        )
                    }
                )

                DetailsTab.ContentProviders -> ContentProvidersTab(
                    contentProviders = packageDetailsUiState.filteredContentProviders,
                    searchText = packageDetailsUiState.contentProviderSearchText,
                    onContentProviderSearchChange = {
                        packageDetailsViewModel.handleIntent(
                            PackageDetailsIntent.FilterContentProviders(it)
                        )
                    }
                )

                DetailsTab.AppLinks -> AppLinksTab(
                    appLinks = packageDetailsUiState.appLinks
                )
            }
        }
    }
}
