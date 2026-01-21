package com.github.hakandindis.plugins.adbhub.models

/**
 * Detailed information about an Android device
 */
data class DeviceInfo(
    val deviceId: String,
    val model: String?,
    val manufacturer: String?,
    val product: String?,
    val androidVersion: String?,
    val apiLevel: String?,
    val sdkVersion: String?,
    val buildNumber: String?,
    val buildFingerprint: String?,
    val screenResolution: String?,
    val screenDensity: String?,
    val cpuAbi: String?,
    val hardware: String?
) {
    val displayName: String
        get() = model ?: product ?: deviceId

    val androidInfo: String
        get() = buildString {
            androidVersion?.let { append("Android $it") }
            apiLevel?.let {
                if (isNotEmpty()) append(" ")
                append("(API $it)")
            }
        }
}
