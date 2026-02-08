package com.github.hakandindis.plugins.adbhub.models

/**
 * Represents a permission section from dumpsys package output
 * (e.g. declared permissions, requested permissions, install permissions, runtime permissions)
 */
data class PermissionSection(
    val sectionType: PermissionSectionType,
    val sectionTitle: String,
    val items: List<PermissionItem>
)

/**
 * Single permission entry within a section
 * @param name Permission name (e.g. android.permission.CAMERA)
 * @param detail Optional detail (e.g. "prot=signature", "granted=true", "granted=false")
 */
data class PermissionItem(
    val name: String,
    val detail: String? = null
)
