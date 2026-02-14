package com.github.hakandindis.plugins.adbhub.core.selection

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectionManagerImpl(
    @Suppress("unused") private val project: Project
) : SelectionManager {

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    override val selectedDeviceState: StateFlow<Device?> = _selectedDevice.asStateFlow()

    private val _selectedPackage = MutableStateFlow<ApplicationPackage?>(null)
    override val selectedPackageState: StateFlow<ApplicationPackage?> = _selectedPackage.asStateFlow()

    override fun selectDevice(device: Device?) {
        _selectedDevice.value = device
        _selectedPackage.value = null
    }

    override fun selectPackage(packageItem: ApplicationPackage?) {
        _selectedPackage.value = packageItem
    }
}
