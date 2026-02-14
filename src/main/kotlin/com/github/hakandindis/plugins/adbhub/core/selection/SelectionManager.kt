package com.github.hakandindis.plugins.adbhub.core.selection

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import kotlinx.coroutines.flow.StateFlow

interface SelectionManager {
    val selectedDeviceState: StateFlow<Device?>
    val selectedPackageState: StateFlow<ApplicationPackage?>
    fun selectDevice(device: Device?)
    fun selectPackage(packageItem: ApplicationPackage?)
}
