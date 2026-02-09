package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

import com.github.hakandindis.plugins.adbhub.models.PermissionSectionType

data class PermissionSectionUiModel(
    val sectionType: PermissionSectionType,
    val sectionTitle: String,
    val items: List<PermissionItemUiModel>
)

sealed class PermissionItemDisplay {
    data object GrantedBadge : PermissionItemDisplay()
    data object DeniedBadge : PermissionItemDisplay()
    data class DetailText(val text: String) : PermissionItemDisplay()
}

data class PermissionItemUiModel(
    val name: String,
    val permissionDisplay: PermissionItemDisplay,
    val permissionType: PermissionSectionType
) {
    val detailTextForFilter: String
        get() = when (permissionDisplay) {
            is PermissionItemDisplay.DetailText -> permissionDisplay.text
            else -> ""
        }
}
