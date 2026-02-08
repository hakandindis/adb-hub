package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ContentProviderUiModel
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Mapper for converting ProviderInfo to UI models
 */
object ContentProviderMapper {
    fun toUiModel(provider: PackageDetails.ProviderInfo): ContentProviderUiModel {
        return ContentProviderUiModel(
            name = provider.name,
            shortName = provider.name.substringAfterLast("/"),
            exported = provider.exported,
            enabled = provider.enabled
        )
    }

    fun toUiModels(providers: List<PackageDetails.ProviderInfo>): List<ContentProviderUiModel> {
        return providers.map { toUiModel(it) }
    }
}
