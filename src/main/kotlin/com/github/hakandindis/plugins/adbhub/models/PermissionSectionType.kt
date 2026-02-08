package com.github.hakandindis.plugins.adbhub.models

/**
 * Type of permission section from dumpsys package output.
 * Use this instead of hardcoded section title strings when branching on section type.
 *
 * [priority] defines display order in the Permissions tab (lower = shown first).
 * Order: RUNTIME → INSTALL → REQUESTED → DECLARED.
 */
enum class PermissionSectionType(val priority: Int) {
    RUNTIME(0),
    INSTALL(1),
    REQUESTED(2),
    DECLARED(3);

    /** Whether this section shows granted status (granted=true/false) with a visual badge */
    fun showsGrantedBadge(): Boolean = this == INSTALL || this == RUNTIME
}
