package com.github.hakandindis.plugins.adbhub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.hakandindis.plugins.adbhub.core.adb.AdbInitializer
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.feature.console_log.presentation.ConsoleLogViewModel
import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.DeviceIntent
import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.DeviceViewModel
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation.PackageListIntent
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation.PackageListViewModel
import com.github.hakandindis.plugins.adbhub.feature.main.AdbMainContent
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsIntent
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
import com.github.hakandindis.plugins.adbhub.feature.sidebar.AdbSidebar
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme

@Composable
fun AdbToolContent(
    adbInitializer: AdbInitializer,
    deviceViewModel: DeviceViewModel,
    packageListViewModel: PackageListViewModel,
    packageDetailsViewModel: PackageDetailsViewModel,
    packageActionsViewModel: PackageActionsViewModel,
    consoleLogViewModel: ConsoleLogViewModel
) {
    val deviceUiState by deviceViewModel.uiState.collectAsState()
    val devices = deviceUiState.devices
    val selectedDevice = deviceUiState.selectedDevice
    val deviceInfoItems = deviceUiState.deviceInfoItems

    val packageListUiState by packageListViewModel.uiState.collectAsState()
    val filteredPackages = packageListUiState.filteredPackages
    val selectedPackage = packageListUiState.selectedPackage
    val packageSearchText = packageListUiState.searchText

    LaunchedEffect(deviceViewModel) {
        deviceViewModel.handleIntent(DeviceIntent.RefreshDevices)
    }

    LaunchedEffect(selectedDevice) {
        selectedDevice?.let { device ->
            if (device.state == DeviceState.DEVICE) {
                packageListViewModel.handleIntent(
                    PackageListIntent.RefreshPackages(device.id, includeSystemApps = true)
                )
            }
        }
    }

    adbInitializer.isAdbAvailable()

    LaunchedEffect(selectedPackage, selectedDevice) {
        selectedPackage?.let { packageItem ->
            selectedDevice?.let { device ->
                if (device.state == DeviceState.DEVICE) {
                    packageDetailsViewModel.handleIntent(
                        PackageDetailsIntent.LoadPackageDetails(packageItem.packageName, device.id)
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdbHubTheme.background)
    ) {
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AdbSidebar(
                devices = devices,
                selectedDevice = selectedDevice,
                deviceInfoItems = deviceInfoItems,
                packages = filteredPackages,
                selectedPackage = selectedPackage,
                searchText = packageSearchText,
                onSearchChange = { text ->
                    packageListViewModel.handleIntent(PackageListIntent.SearchPackages(text))
                },
                onDeviceSelected = { device ->
                    deviceViewModel.handleIntent(DeviceIntent.SelectDevice(device))
                },
                onRefreshDevices = {
                    deviceViewModel.handleIntent(DeviceIntent.RefreshDevices)
                },
                onPackageSelected = { packageItem ->
                    packageListViewModel.handleIntent(PackageListIntent.SelectPackage(packageItem))
                }
            )
            AdbMainContent(
                packageDetailsViewModel = packageDetailsViewModel,
                packageActionsViewModel = packageActionsViewModel,
                consoleLogViewModel = consoleLogViewModel,
                selectedDevice = selectedDevice,
                selectedPackage = selectedPackage,
                uid = null, // TODO: Get UID from package details or device info
                onCopyPath = { path ->
                    // TODO: Copy to clipboard
                },
                onActivityLaunch = { activityName ->
                    selectedDevice?.let { device ->
                        packageDetailsViewModel.handleIntent(
                            PackageDetailsIntent.LaunchActivity(activityName, device.id)
                        )
                    }
                }
            )
        }
    }
}
