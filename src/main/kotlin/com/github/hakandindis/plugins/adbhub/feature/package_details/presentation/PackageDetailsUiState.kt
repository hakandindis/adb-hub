package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.*

/**
 * UI State for Package Details feature.
 * Holds UI models for activities, services, receivers, content providers and their filtered/search state.
 */
data class PackageDetailsUiState(
    val generalInfoItems: List<InfoItemUiModel> = emptyList(),
    val activities: List<ActivityUiModel> = emptyList(),
    val activitySearchText: String = "",
    val filteredActivities: List<ActivityUiModel> = emptyList(),
    val services: List<ServiceUiModel> = emptyList(),
    val serviceSearchText: String = "",
    val filteredServices: List<ServiceUiModel> = emptyList(),
    val receivers: List<ReceiverUiModel> = emptyList(),
    val receiverSearchText: String = "",
    val filteredReceivers: List<ReceiverUiModel> = emptyList(),
    val contentProviders: List<ContentProviderUiModel> = emptyList(),
    val contentProviderSearchText: String = "",
    val filteredContentProviders: List<ContentProviderUiModel> = emptyList(),
    val permissionSections: List<PermissionSectionUiModel> = emptyList(),
    val permissionSearchText: String = "",
    val filteredPermissionSections: List<PermissionSectionUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
