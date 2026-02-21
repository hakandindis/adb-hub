package com.github.hakandindis.plugins.adbhub.constants

object ParsePatterns {
    val VERSION_NAME = "versionName=(\\S+)".toRegex()
    val VERSION_CODE = "versionCode=(\\d+)".toRegex()
    val TARGET_SDK = "targetSdk=(\\d+)".toRegex()
    val TARGET_SDK_VERSION = "targetSdkVersion=(\\d+)".toRegex()
    val MIN_SDK = "minSdk=(\\d+)".toRegex()
    val MIN_SDK_VERSION = "minSdkVersion=(\\d+)".toRegex()
    val DATA_DIR = "dataDir=(\\S+)".toRegex()
    val CODE_PATH = "codePath=(\\S+)".toRegex()
    val USER_ID = "userId=(\\d+)".toRegex()

    val FIRST_INSTALL_TIME = "firstInstallTime=(\\d+)".toRegex()
    val LAST_UPDATE_TIME = "lastUpdateTime=(\\d+)".toRegex()

    val ACTIVITY_PATTERN_1 =
        "Activity\\s+#?\\d+:\\s*name=([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val ACTIVITY_PATTERN_2 =
        "Activity\\s+[a-f0-9]+\\s+([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val SERVICE_PATTERN_1 =
        "Service\\s+#?\\d+:\\s*name=([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val SERVICE_PATTERN_2 =
        "Service\\s+[a-f0-9]+\\s+([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val RECEIVER_PATTERN_1 =
        "Receiver\\s+#?\\d+:\\s*name=([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val RECEIVER_PATTERN_2 =
        "Receiver\\s+[a-f0-9]+\\s+([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val PROVIDER_PATTERN_1 =
        "Provider\\s+#?\\d+:\\s*name=([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)
    val PROVIDER_PATTERN_2 =
        "Provider\\s+[a-f0-9]+\\s+([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.$]+)".toRegex(RegexOption.IGNORE_CASE)

    val RESOLVER_TABLE_COMPONENT =
        "[a-f0-9]+\\s+([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.\$]+)\\s+filter".toRegex()

    val REGISTERED_PROVIDER_LINE = "\\s*([a-zA-Z0-9_.]+)/([a-zA-Z0-9_.\$]+)\\s*[:}]".toRegex()

    val FILTER_PATTERN_1 = "Filter\\s+#\\d+".toRegex(RegexOption.IGNORE_CASE)
    val FILTER_PATTERN_2 = "Intent\\s+Filter\\s+#\\d+".toRegex(RegexOption.IGNORE_CASE)
    val ACTION_PATTERN_1 = "Action:\\s*\"([^\"]+)\"".toRegex(RegexOption.IGNORE_CASE)
    val ACTION_PATTERN_2 = "Action:\\s*([a-zA-Z0-9_.]+)".toRegex(RegexOption.IGNORE_CASE)
    val CATEGORY_PATTERN_1 = "Category:\\s*\"([^\"]+)\"".toRegex(RegexOption.IGNORE_CASE)
    val CATEGORY_PATTERN_2 = "Category:\\s*([a-zA-Z0-9_.]+)".toRegex(RegexOption.IGNORE_CASE)
    val DATA_PATTERN_1 = "Data:\\s*\"([^\"]+)\"".toRegex(RegexOption.IGNORE_CASE)
    val DATA_PATTERN_2 = "Data:\\s*([a-zA-Z0-9_.:/]+)".toRegex(RegexOption.IGNORE_CASE)

    val PERMISSION_LINE = "([a-zA-Z][a-zA-Z0-9_.]*permission[a-zA-Z0-9_.]*)".toRegex()
    val PERMISSION_FULL = "([a-zA-Z][a-zA-Z0-9_.]*\\.permission\\.[a-zA-Z0-9_]+)".toRegex()

    val ENABLED_PATTERN = "enabled[=:]\\s*(true|false)".toRegex(RegexOption.IGNORE_CASE)
    val EXPORTED_TRUE = "exported=true"
    val EXPORTED_TRUE_ALT = "exported: true"
    val EXPORTED_FALSE = "exported=false"
    val EXPORTED_FALSE_ALT = "exported: false"
    val ENABLED_TRUE = "enabled=true"
    val ENABLED_TRUE_ALT = "enabled: true"
    val ENABLED_FALSE = "enabled=false"
    val ENABLED_FALSE_ALT = "enabled: false"

    val GRANTED_PATTERN = "granted=true.*?".toRegex(RegexOption.IGNORE_CASE)
    val DENIED_PATTERN = "granted=false.*?".toRegex(RegexOption.IGNORE_CASE)
}
