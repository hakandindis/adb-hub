package com.github.hakandindis.plugins.adbhub.models

/**
 * Permission status information
 */
data class PermissionStatus(
    val permission: String,
    val status: PermissionGrantStatus,
    val icon: String? = null // Optional icon identifier
)

enum class PermissionGrantStatus {
    GRANTED,
    DENIED,
    OPTIONAL
}
