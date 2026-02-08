package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.*
import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo

/**
 * View-ready result of loading package details.
 * All mapping from domain/data models to UI models is done in the use case.
 */
data class GetPackageDetailsResult(
    val generalInfoItems: List<InfoItemUiModel>,
    val activities: List<ActivityUiModel>,
    val permissionSections: List<PermissionSectionUiModel>,
    val services: List<ServiceUiModel>,
    val receivers: List<ReceiverUiModel>,
    val contentProviders: List<ContentProviderUiModel>,
    val appLinks: AppLinksInfo? = null
)
