package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.hakandindis.plugins.adbhub.core.models.Device
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
    searchText: String,
    onSearchChange: (String) -> Unit,
    onDeviceSelected: (Device) -> Unit,
    onRefreshDevices: () -> Unit,
    onPackageSelected: (ApplicationPackage) -> Unit
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
            onDeviceSelected = onDeviceSelected,
            onRefreshDevices = onRefreshDevices
        )
        Divider(Orientation.Horizontal, modifier = Modifier.fillMaxWidth())
        Column(modifier = Modifier.weight(1f)) {
            InstalledPackagesSection(
                packages = packages,
                selectedPackage = selectedPackage,
                searchText = searchText,
                onSearchChange = onSearchChange,
                onPackageSelected = onPackageSelected
            )
        }
    }
}
