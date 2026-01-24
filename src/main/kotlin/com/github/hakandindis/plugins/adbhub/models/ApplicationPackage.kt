package com.github.hakandindis.plugins.adbhub.models

/**
 * Represents an installed Android package/application
 */
data class ApplicationPackage(
    val packageName: String,
    val versionName: String? = null,
    val versionCode: String? = null,
    val installLocation: String? = null,
    val dataDir: String? = null,
    val isSystemApp: Boolean = false,
    val isEnabled: Boolean = true
) {

    val displayName: String
        get() {
            val name = if (packageName.contains(".")) {
                packageName.substringAfterLast(".")
            } else {
                packageName
            }
            return name.ifBlank { packageName }
        }

}
