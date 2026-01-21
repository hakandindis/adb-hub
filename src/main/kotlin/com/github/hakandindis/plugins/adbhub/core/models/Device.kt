package com.github.hakandindis.plugins.adbhub.core.models

/**
 * Represents an Android device connected via ADB
 */
data class Device(
    val id: String,
    val state: DeviceState,
    val model: String? = null,
    val product: String? = null,
    val transportId: String? = null
) {
    val displayName: String
        get() = model ?: product ?: id
}

enum class DeviceState {
    DEVICE,
    OFFLINE,
    UNAUTHORIZED,
    UNKNOWN
}
