package com.github.hakandindis.plugins.adbhub.models

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

    val initials: String
        get() = displayName
            .takeIf { it.length >= 2 }
            ?.take(2)
            ?.uppercase()
            ?: packageName
                .takeIf { it.length >= 2 }
                ?.take(2)
                ?.uppercase()
            ?: "AP"
}
