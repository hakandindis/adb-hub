package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.*

/**
 * UI State for Package Details feature
 */
data class PackageDetailsUiState(
    val generalInfoItems: List<InfoItemUiModel> = emptyList(),
    val pathItems: List<PathItemUiModel> = emptyList(),
    val activities: List<ActivityUiModel> = emptyList(),
    val permissions: List<PermissionUiModel> = emptyList(),
    val certificateItems: List<CertificateItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
