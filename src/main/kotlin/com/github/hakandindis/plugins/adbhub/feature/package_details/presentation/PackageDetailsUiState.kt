package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.InfoItemUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.PermissionSectionUiModel
import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo

/**
 * UI State for Package Details feature.
 * Holds UI models for activities, services, receivers, content providers and their filtered/search state.
 */
data class PackageDetailsUiState(
    val packageName: String = "",
    val appName: String = "",
    val generalInfoItems: List<InfoItemUiModel> = emptyList(),
    val activities: List<ComponentDisplay> = emptyList(),
    val activitySearchText: String = "",
    val filteredActivities: List<ComponentDisplay> = emptyList(),
    val services: List<ComponentDisplay> = emptyList(),
    val serviceSearchText: String = "",
    val filteredServices: List<ComponentDisplay> = emptyList(),
    val receivers: List<ComponentDisplay> = emptyList(),
    val receiverSearchText: String = "",
    val filteredReceivers: List<ComponentDisplay> = emptyList(),
    val contentProviders: List<ComponentDisplay> = emptyList(),
    val contentProviderSearchText: String = "",
    val filteredContentProviders: List<ComponentDisplay> = emptyList(),
    val permissionSections: List<PermissionSectionUiModel> = emptyList(),
    val permissionSearchText: String = "",
    val filteredPermissionSections: List<PermissionSectionUiModel> = emptyList(),
    val appLinks: AppLinksInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
