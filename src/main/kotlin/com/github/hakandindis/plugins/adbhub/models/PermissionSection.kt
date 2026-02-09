package com.github.hakandindis.plugins.adbhub.models

data class PermissionSection(
    val sectionType: PermissionSectionType,
    val sectionTitle: String,
    val items: List<PermissionItem>
)

data class PermissionItem(
    val name: String,
    val detail: String? = null
)
