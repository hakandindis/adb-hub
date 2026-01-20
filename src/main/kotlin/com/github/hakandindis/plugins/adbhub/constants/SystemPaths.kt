package com.github.hakandindis.plugins.adbhub.constants

/**
 * Android system path constants
 */
object SystemPaths {
    const val SYSTEM = "/system/"
    const val VENDOR = "/vendor/"
    const val PRODUCT = "/product/"
    const val ODM = "/odm/"
    const val APEX = "/apex/"
    const val PRIV_APP = "/priv-app/"

    /**
     * List of all system app paths
     */
    val SYSTEM_APP_PATHS = listOf(
        SYSTEM,
        VENDOR,
        PRODUCT,
        ODM,
        APEX,
        PRIV_APP
    )

    /**
     * Checks if a path is a system app path
     */
    fun isSystemAppPath(path: String?): Boolean {
        if (path == null) return false
        return SYSTEM_APP_PATHS.any { path.contains(it) }
    }
}
