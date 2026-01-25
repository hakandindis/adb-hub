package com.github.hakandindis.plugins.adbhub.feature.devices.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.ui.DeviceInfoItemUiModel
import com.github.hakandindis.plugins.adbhub.models.DeviceInfo

/**
 * Mapper for converting DeviceInfo to UI models (label-value list)
 */
object DeviceInfoMapper {

    private const val PLACEHOLDER = "—"

    fun toInfoItems(deviceInfo: DeviceInfo): List<DeviceInfoItemUiModel> {
        return listOf(
            DeviceInfoItemUiModel("Manufacturer", deviceInfo.manufacturer ?: PLACEHOLDER),
            DeviceInfoItemUiModel("Android Ver", deviceInfo.androidInfo.takeIf { it.isNotBlank() } ?: PLACEHOLDER),
            DeviceInfoItemUiModel("Physical size", deviceInfo.screenResolution ?: PLACEHOLDER),
            DeviceInfoItemUiModel("Density", deviceInfo.screenDensity ?: PLACEHOLDER),
            DeviceInfoItemUiModel("CPU/ABI", deviceInfo.cpuAbi ?: PLACEHOLDER)
        )
    }
}
