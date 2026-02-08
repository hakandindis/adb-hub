package com.github.hakandindis.plugins.adbhub.constants

/**
 * String constants used for parsing dumpsys output
 */
object DumpsysParseStrings {
    // Section headers
    const val REQUESTED_PERMISSIONS = "requested permissions:"
    const val GRANTED_PERMISSIONS = "granted permissions:"
    const val DECLARED_PERMISSIONS = "declared permissions:"
    const val INSTALL_PERMISSIONS = "install permissions:"
    const val RUNTIME_PERMISSIONS = "runtime permissions:"

    /** Section headers that follow runtime permissions in dumpsys; parsing must stop before these */
    const val ENABLED_COMPONENTS_HEADER = "enabledComponents"
    const val DISABLED_COMPONENTS_HEADER = "disabledComponents"

    // Package property keys
    const val PACKAGE_PREFIX = "package:"
    const val VERSION_NAME = "versionName="
    const val VERSION_CODE = "versionCode="
    const val TARGET_SDK = "targetSdk="
    const val TARGET_SDK_VERSION = "targetSdkVersion="
    const val MIN_SDK = "minSdk="
    const val MIN_SDK_VERSION = "minSdkVersion="
    const val DATA_DIR = "dataDir="
    const val ENABLED = "enabled"
    const val EXPORTED = "exported"
    const val FIRST_INSTALL_TIME = "firstInstallTime="
    const val LAST_UPDATE_TIME = "lastUpdateTime="

    // Component prefixes
    const val ACTIVITY_PREFIX = "Activity"
    const val SERVICE_PREFIX = "Service"
    const val RECEIVER_PREFIX = "Receiver"
    const val PROVIDER_PREFIX = "Provider"

    // Intent filter prefixes
    const val FILTER_PREFIX = "Filter"
    const val INTENT_FILTER_PREFIX = "Intent Filter"
    const val ACTION_PREFIX = "Action:"
    const val CATEGORY_PREFIX = "Category:"
    const val DATA_PREFIX = "Data:"

    // Boolean values
    const val TRUE = "true"
    const val FALSE = "false"
}
