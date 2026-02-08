package com.github.hakandindis.plugins.adbhub.models

enum class PermissionSectionType(val priority: Int) {
    RUNTIME(0),
    INSTALL(1),
    REQUESTED(2),
    DECLARED(3);

    fun showsGrantedBadge(): Boolean = this == INSTALL || this == RUNTIME
}
