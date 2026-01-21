package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.dimens.AdbHubDimens
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider

@Composable
fun AdbSidebar(
    devices: List<Device>,
    selectedDevice: Device?,
    deviceInfo: DeviceInfo?,
    packages: List<ApplicationPackage>,
    selectedPackage: ApplicationPackage?,
    packageDetailsVersion: String?,
    searchText: String,
    showSystem: Boolean,
    showUser: Boolean,
    showDebug: Boolean,
    packageActionsViewModel: PackageActionsViewModel?,
    onSearchChange: (String) -> Unit,
    onShowSystemChange: (Boolean) -> Unit,
    onShowUserChange: (Boolean) -> Unit,
    onShowDebugChange: (Boolean) -> Unit,
    onDeviceSelected: (Device) -> Unit,
    onPackageSelected: (ApplicationPackage) -> Unit,
    onLaunch: () -> Unit,
    onForceStop: () -> Unit,
    onClearData: () -> Unit,
    onUninstall: () -> Unit,
    onDeepLink: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(AdbHubDimens.Layout.SIDEBAR_WIDTH)
            .fillMaxHeight()
            .background(AdbHubTheme.surface)
    ) {
        TargetDeviceSection(
            devices = devices,
            selectedDevice = selectedDevice,
            deviceInfo = deviceInfo,
            onDeviceSelected = onDeviceSelected
        )
        Divider(Orientation.Horizontal, modifier = Modifier.fillMaxWidth())
        Column(modifier = Modifier.weight(1f)) {
            InstalledPackagesSection(
                packages = packages,
                selectedPackage = selectedPackage,
                searchText = searchText,
                showSystem = showSystem,
                showUser = showUser,
                showDebug = showDebug,
                onSearchChange = onSearchChange,
                onShowSystemChange = onShowSystemChange,
                onShowUserChange = onShowUserChange,
                onShowDebugChange = onShowDebugChange,
                onPackageSelected = onPackageSelected
            )
        }
        if (selectedPackage != null) {
            Divider(Orientation.Horizontal, modifier = Modifier.fillMaxWidth())
            QuickActionsSection(
                packageId = selectedPackage.packageName,
                versionName = packageDetailsVersion ?: selectedPackage.versionName,
                deviceId = selectedDevice?.id ?: "",
                packageActionsViewModel = packageActionsViewModel,
                onLaunch = onLaunch,
                onForceStop = onForceStop,
                onClearData = onClearData,
                onUninstall = onUninstall,
                onDeepLink = onDeepLink
            )
        }
    }
}
