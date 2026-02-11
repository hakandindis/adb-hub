package com.github.hakandindis.plugins.adbhub.core.selection

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectionManagerImpl(
    @Suppress("unused") private val project: Project
) : SelectionManager {

    private val _selectionState = MutableStateFlow(SelectionState())
    override val selectionState: StateFlow<SelectionState> = _selectionState.asStateFlow()

    override fun selectDevice(device: Device?) {
        _selectionState.update {
            it.copy(
                selectedDevice = device,
                selectedPackage = null
            )
        }
    }

    override fun selectPackage(packageItem: ApplicationPackage?) {
        _selectionState.update { it.copy(selectedPackage = packageItem) }
    }
}
