package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ActivityUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel

/**
 * UI State for Package Details feature
 */
data class PackageDetailsUiState(
    val generalInfoItems: List<InfoItemUiModel> = emptyList(),
    val activities: List<ActivityUiModel> = emptyList(),
    val permissionSections: List<PermissionSectionUiModel> = emptyList(),
    val permissionSearchText: String = "",
    val filteredPermissionSections: List<PermissionSectionUiModel> = emptyList(),
    val activitySearchText: String = "",
    val filteredActivities: List<ActivityUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
