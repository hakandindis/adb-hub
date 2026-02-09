package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel
import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo

data class GetPackageDetailsResult(
    val packageName: String,
    val appName: String,
    val generalInfoItems: List<InfoItemUiModel>,
    val activities: List<ComponentDisplay>,
    val permissionSections: List<PermissionSectionUiModel>,
    val services: List<ComponentDisplay>,
    val receivers: List<ComponentDisplay>,
    val contentProviders: List<ComponentDisplay>,
    val appLinks: AppLinksInfo? = null
)
