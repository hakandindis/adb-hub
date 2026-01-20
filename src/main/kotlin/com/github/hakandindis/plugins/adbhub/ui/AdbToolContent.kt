package com.github.hakandindis.plugins.adbhub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.github.hakandindis.plugins.adbhub.core.adb.AdbInitializer
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.feature.console_log.presentation.ConsoleLogViewModel
import com.github.hakandindis.plugins.adbhub.feature.device.presentation.DeviceIntent
import com.github.hakandindis.plugins.adbhub.feature.device.presentation.DeviceViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsIntent
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsIntent
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
import com.github.hakandindis.plugins.adbhub.feature.packages.presentation.PackageListIntent
import com.github.hakandindis.plugins.adbhub.feature.packages.presentation.PackageListViewModel
import com.github.hakandindis.plugins.adbhub.ui.components.AdbMainContent
import com.github.hakandindis.plugins.adbhub.ui.components.AdbSidebar
import com.github.hakandindis.plugins.adbhub.ui.components.AdbToolbar
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme

@Composable
fun AdbToolContent(
    adbInitializer: AdbInitializer,
    deviceViewModel: DeviceViewModel?,
    packageListViewModel: PackageListViewModel?,
    packageDetailsViewModel: PackageDetailsViewModel?,
    packageActionsViewModel: PackageActionsViewModel?,
    consoleLogViewModel: ConsoleLogViewModel?
) {
    val deviceUiState = deviceViewModel?.uiState?.collectAsState()?.value
    val devices = deviceUiState?.devices ?: emptyList()
    val selectedDevice = deviceUiState?.selectedDevice
    val deviceInfo = deviceUiState?.deviceInfo

    val packageListUiState = packageListViewModel?.uiState?.collectAsState()?.value
    val filteredPackages = packageListUiState?.filteredPackages ?: emptyList()
    val selectedPackage = packageListUiState?.selectedPackage
    val packageSearchText = packageListUiState?.searchText ?: ""
    val showSystemApps = packageListUiState?.showSystemApps ?: false
    val showUserApps = packageListUiState?.showUserApps ?: true
    val showDebugApps = packageListUiState?.showDebugApps ?: false

    LaunchedEffect(deviceViewModel) {
        deviceViewModel?.handleIntent(DeviceIntent.RefreshDevices)
    }

    LaunchedEffect(selectedDevice) {
        selectedDevice?.let { device ->
            if (device.state == DeviceState.DEVICE) {
                packageListViewModel?.handleIntent(
                    PackageListIntent.RefreshPackages(device.id, includeSystemApps = true)
                )
            }
        }
    }
    val packageDetailsUiState = packageDetailsViewModel?.uiState?.collectAsState()?.value
    val packageActionsUiState = packageActionsViewModel?.uiState?.collectAsState()?.value
    val isAdbAvailable = adbInitializer.isAdbAvailable()

    LaunchedEffect(selectedPackage, selectedDevice) {
        selectedPackage?.let { packageItem ->
            selectedDevice?.let { device ->
                if (device.state == DeviceState.DEVICE) {
                    packageDetailsViewModel?.handleIntent(
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
        AdbToolbar(isAdbConnected = isAdbAvailable)
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AdbSidebar(
                devices = devices,
                selectedDevice = selectedDevice,
                deviceInfo = deviceInfo,
                packages = filteredPackages,
                selectedPackage = selectedPackage,
                packageDetailsVersion = packageDetailsUiState?.generalInfoItems
                    ?.firstOrNull { it.label == "Version Name" }?.value?.takeIf { it != "N/A" },
                searchText = packageSearchText,
                showSystem = showSystemApps,
                showUser = showUserApps,
                showDebug = showDebugApps,
                packageActionsViewModel = packageActionsViewModel,
                onSearchChange = { text ->
                    packageListViewModel?.handleIntent(PackageListIntent.SetSearchText(text))
                },
                onShowSystemChange = { show ->
                    packageListViewModel?.handleIntent(PackageListIntent.SetShowSystemApps(show))
                },
                onShowUserChange = { show ->
                    packageListViewModel?.handleIntent(PackageListIntent.SetShowUserApps(show))
                },
                onShowDebugChange = { show ->
                    packageListViewModel?.handleIntent(PackageListIntent.SetShowDebugApps(show))
                },
                onDeviceSelected = { device ->
                    deviceViewModel?.handleIntent(DeviceIntent.SelectDevice(device))
                },
                onPackageSelected = { packageItem ->
                    packageListViewModel?.handleIntent(PackageListIntent.SelectPackage(packageItem))
                },
                onLaunch = {
                    selectedPackage?.let { packageItem ->
                        selectedDevice?.let { device ->
                            packageActionsViewModel?.handleIntent(
                                PackageActionsIntent.LaunchApp(packageItem.packageName, device.id)
                            )
                        }
                    }
                },
                onForceStop = {
                    selectedPackage?.let { packageItem ->
                        selectedDevice?.let { device ->
                            packageActionsViewModel?.handleIntent(
                                PackageActionsIntent.ForceStop(packageItem.packageName, device.id)
                            )
                        }
                    }
                },
                onClearData = {
                    selectedPackage?.let { packageItem ->
                        selectedDevice?.let { device ->
                            packageActionsViewModel?.handleIntent(
                                PackageActionsIntent.ClearData(packageItem.packageName, device.id)
                            )
                        }
                    }
                },
                onUninstall = {
                    selectedPackage?.let { packageItem ->
                        selectedDevice?.let { device ->
                            packageActionsViewModel?.handleIntent(
                                PackageActionsIntent.Uninstall(packageItem.packageName, device.id)
                            )
                        }
                    }
                },
                onDeepLink = { deepLink ->
                    selectedDevice?.let { device ->
                        packageActionsViewModel?.handleIntent(
                            PackageActionsIntent.LaunchDeepLink(deepLink, device.id)
                        )
                    }
                }
            )
            AdbMainContent(
                packageDetailsViewModel = packageDetailsViewModel,
                packageDetailsUiState = packageDetailsUiState,
                packageActionsViewModel = packageActionsViewModel,
                packageActionsUiState = packageActionsUiState,
                packageListUiState = packageListUiState,
                consoleLogViewModel = consoleLogViewModel,
                consoleLogUiState = null,
                selectedDevice = selectedDevice,
                uid = null, // TODO: Get UID from package details or device info
                onCopyPath = { path ->
                    // TODO: Copy to clipboard
                },
                onActivityLaunch = { activityName ->
                    selectedDevice?.let { device ->
                        packageDetailsViewModel?.handleIntent(
                            PackageDetailsIntent.LaunchActivity(activityName, device.id)
                        )
                    }
                }
            )
        }
    }
}
