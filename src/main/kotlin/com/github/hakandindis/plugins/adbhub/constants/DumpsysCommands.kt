package com.github.hakandindis.plugins.adbhub.constants

/**
 * Dumpsys command constants
 */
object DumpsysCommands {
    private const val SHELL_PREFIX = "shell"
    private const val DUMPsys = "dumpsys"

    /**
     * Gets dumpsys package output for a specific package
     * Usage: shell dumpsys package <packageName>
     */
    fun getPackageDumpsys(packageName: String): String {
        return "$SHELL_PREFIX $DUMPsys package $packageName"
    }
}
