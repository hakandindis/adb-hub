package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ReceiverUiModel
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Mapper for converting ReceiverInfo to UI models
 */
object ReceiverMapper {
    fun toUiModel(receiver: PackageDetails.ReceiverInfo): ReceiverUiModel {
        return ReceiverUiModel(
            name = receiver.name,
            shortName = receiver.name.substringAfterLast("/"),
            exported = receiver.exported,
            enabled = receiver.enabled
        )
    }

    fun toUiModels(receivers: List<PackageDetails.ReceiverInfo>): List<ReceiverUiModel> {
        return receivers.map { toUiModel(it) }
    }
}
