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

    val fullInfo: String
        get() = buildString {
            append(packageName)
            versionName?.let {
                append(" (v$it")
                versionCode?.let { append(", code: $it") }
                append(")")
            }
            if (isSystemApp) append(" [System]")
            if (!isEnabled) append(" [Disabled]")
        }

    /**
     * Checks if this is a Google app based on package name
     */
    val isGoogleApp: Boolean
        get() = packageName.startsWith("com.google.") ||
                packageName.startsWith("com.android.") ||
                packageName in GOOGLE_PACKAGE_NAMES

    companion object {
        /**
         * Common Google package names that don't start with com.google.*
         */
        private val GOOGLE_PACKAGE_NAMES = setOf(
            "com.android.chrome",
            "com.android.vending",
            "com.android.providers.contacts",
            "com.android.providers.media",
            "com.android.providers.downloads",
            "com.android.providers.settings",
            "com.android.providers.telephony",
            "com.android.providers.calendar",
            "com.android.calendar",
            "com.android.contacts",
            "com.android.dialer",
            "com.android.mms",
            "com.android.email",
            "com.android.music",
            "com.android.gallery3d",
            "com.android.camera2",
            "com.android.documentsui",
            "com.android.packageinstaller",
            "com.android.settings",
            "com.android.systemui",
            "com.android.wallpaper",
            "com.android.launcher",
            "com.android.launcher2",
            "com.android.launcher3"
        )
    }
}
