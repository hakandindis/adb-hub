package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ServiceUiModel
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Mapper for converting ServiceInfo to UI models
 */
object ServiceMapper {
    fun toUiModel(service: PackageDetails.ServiceInfo): ServiceUiModel {
        return ServiceUiModel(
            name = service.name,
            shortName = service.name.substringAfterLast("/"),
            exported = service.exported,
            enabled = service.enabled
        )
    }

    fun toUiModels(services: List<PackageDetails.ServiceInfo>): List<ServiceUiModel> {
        return services.map { toUiModel(it) }
    }
}
