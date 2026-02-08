package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

import com.github.hakandindis.plugins.adbhub.models.PermissionSectionType

/**
 * UI model for a permission section (e.g. Declared permissions, Runtime permissions)
 */
data class PermissionSectionUiModel(
    val sectionType: PermissionSectionType,
    val sectionTitle: String,
    val items: List<PermissionItemUiModel>
)

/**
 * UI model for a single permission entry within a section
 * @param permissionType Section type this item belongs to (use for UI branching, e.g. show granted badge)
 */
data class PermissionItemUiModel(
    val name: String,
    val detail: String? = null,
    val permissionType: PermissionSectionType
)
