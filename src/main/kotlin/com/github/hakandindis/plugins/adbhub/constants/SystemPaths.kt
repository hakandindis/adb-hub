package com.github.hakandindis.plugins.adbhub.constants

object SystemPaths {
    const val SYSTEM = "/system/"
    const val VENDOR = "/vendor/"
    const val PRODUCT = "/product/"
    const val ODM = "/odm/"
    const val APEX = "/apex/"
    const val PRIV_APP = "/priv-app/"

    val SYSTEM_APP_PATHS = listOf(
        SYSTEM,
        VENDOR,
        PRODUCT,
        ODM,
        APEX,
        PRIV_APP
    )

    fun isSystemAppPath(path: String?): Boolean {
        if (path == null) return false
        return SYSTEM_APP_PATHS.any { path.contains(it) }
    }
}
