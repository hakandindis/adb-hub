package com.github.hakandindis.plugins.adbhub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.core.selection.SelectionManager
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
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme

@Composable
fun AdbToolContent(
    deviceViewModel: DeviceViewModel,
    packageListViewModel: PackageListViewModel,
    packageDetailsViewModel: PackageDetailsViewModel,
    packageActionsViewModel: PackageActionsViewModel,
    consoleLogViewModel: ConsoleLogViewModel,
    selectionManager: SelectionManager
) {
    val deviceUiState by deviceViewModel.uiState.collectAsState()
    val devices = deviceUiState.devices
    val deviceInfoItems = deviceUiState.deviceInfoItems

    val selectionState by selectionManager.selectionState.collectAsState()
    val selectedDevice = selectionState.selectedDevice
    val selectedPackage = selectionState.selectedPackage

    val packageListUiState by packageListViewModel.uiState.collectAsState()
    val filteredPackages = packageListUiState.filteredPackages
    val packageSearchText = packageListUiState.searchText

    LaunchedEffect(Unit) {
        deviceViewModel.handleIntent(DeviceIntent.RefreshDevices)
    }

    val hasValidSelection = selectedPackage != null &&
            selectedDevice != null &&
            selectedDevice.state == DeviceState.DEVICE

    val onSearchChange = remember(packageListViewModel) {
        { text: String ->
            packageListViewModel.handleIntent(PackageListIntent.SearchPackages(text))
        }
    }
    val onDeviceSelected = remember(deviceViewModel) {
        { device: Device ->
            deviceViewModel.handleIntent(DeviceIntent.SelectDevice(device))
        }
    }
    val onRefreshDevices = remember(deviceViewModel) {
        { deviceViewModel.handleIntent(DeviceIntent.RefreshDevices) }
    }
    val onPackageSelected = remember(packageListViewModel) {
        { packageItem: ApplicationPackage ->
            packageListViewModel.handleIntent(PackageListIntent.SelectPackage(packageItem))
        }
    }

    LaunchedEffect(selectedPackage, selectedDevice) {
        if (hasValidSelection) {
            packageDetailsViewModel.handleIntent(
                PackageDetailsIntent.LoadPackageDetails(
                    selectedPackage.packageName,
                    selectedDevice.id
                )
            )
        } else {
            packageDetailsViewModel.handleIntent(PackageDetailsIntent.ClearDetails)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdbHubTheme.background)
            .border(1.dp, AdbHubTheme.border)
    ) {
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AdbSidebar(
                devices = devices,
                selectedDevice = selectedDevice,
                deviceInfoItems = deviceInfoItems,
                packages = filteredPackages,
                selectedPackage = selectedPackage,
                searchText = packageSearchText,
                onSearchChange = onSearchChange,
                onDeviceSelected = onDeviceSelected,
                onRefreshDevices = onRefreshDevices,
                onPackageSelected = onPackageSelected
            )
            AdbMainContent(
                packageDetailsViewModel = packageDetailsViewModel,
                packageActionsViewModel = packageActionsViewModel,
                consoleLogViewModel = consoleLogViewModel,
                selectedDevice = selectedDevice,
                selectedPackage = selectedPackage,
                uid = null // TODO: Get UID from package details or device info
            )
        }
    }
}
