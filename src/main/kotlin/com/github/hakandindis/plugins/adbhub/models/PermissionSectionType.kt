package com.github.hakandindis.plugins.adbhub.models

/**
 * Type of permission section from dumpsys package output.
 * Use this instead of hardcoded section title strings when branching on section type.
 */
enum class PermissionSectionType {
    DECLARED,
    REQUESTED,
    INSTALL,
    RUNTIME;

    /** Whether this section shows granted status (granted=true/false) with a visual badge */
    fun showsGrantedBadge(): Boolean = this == INSTALL || this == RUNTIME
}
