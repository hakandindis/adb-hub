package com.github.hakandindis.plugins.adbhub.core.selection

import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.core.models.DeviceState
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

data class SelectionState(
    val selectedDevice: Device? = null,
    val selectedPackage: ApplicationPackage? = null
) {
    val hasValidSelection: Boolean
        get() = selectedDevice != null &&
                selectedPackage != null &&
                selectedDevice.state == DeviceState.DEVICE
}
