package com.github.hakandindis.plugins.adbhub.models

data class PermissionStatus(
    val permission: String,
    val status: PermissionGrantStatus,
    val icon: String? = null
)

enum class PermissionGrantStatus {
    GRANTED,
    DENIED,
    OPTIONAL
}
