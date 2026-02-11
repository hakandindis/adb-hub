package com.github.hakandindis.plugins.adbhub.feature.devices.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.core.selection.SelectionManager
import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.DeviceViewModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class DeviceViewModelFactory(
    private val project: Project,
    private val coroutineScope: CoroutineScope
) {

    fun create(executor: AdbCommandExecutor): DeviceViewModel {
        val deviceRepository = DeviceModule.createDeviceRepository(executor)
        val getDevicesUseCase = DeviceModule.createGetDevicesUseCase(deviceRepository)
        val getDeviceInfoUseCase = DeviceModule.createGetDeviceInfoUseCase(deviceRepository)
        val selectionManager = project.service<SelectionManager>()
        return DeviceViewModel(
            getDevicesUseCase = getDevicesUseCase,
            getDeviceInfoUseCase = getDeviceInfoUseCase,
            selectionManager = selectionManager,
            coroutineScope = coroutineScope
        )
    }
}
