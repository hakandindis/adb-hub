package com.github.hakandindis.plugins.adbhub.core.selection

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SelectionManagerImpl(
    @Suppress("unused") private val project: Project
) : SelectionManager {

    private val scope = CoroutineScope(SupervisorJob())

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    override val selectedDeviceState: StateFlow<Device?> = _selectedDevice.asStateFlow()

    private val _selectedPackage = MutableStateFlow<ApplicationPackage?>(null)
    override val selectedPackageState: StateFlow<ApplicationPackage?> = _selectedPackage.asStateFlow()

    private val _deviceRefreshRequest = MutableSharedFlow<Unit>(replay = 0)
    override val deviceRefreshRequest: SharedFlow<Unit> = _deviceRefreshRequest.asSharedFlow()

    override fun selectDevice(device: Device?) {
        _selectedDevice.value = device
        _selectedPackage.value = null
    }

    override fun selectPackage(packageItem: ApplicationPackage?) {
        _selectedPackage.value = packageItem
    }

    override fun requestDeviceRefresh() {
        scope.launch { _deviceRefreshRequest.emit(Unit) }
    }
}
