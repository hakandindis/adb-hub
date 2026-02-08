package com.github.hakandindis.plugins.adbhub.constants

object DumpsysCommands {
    private const val SHELL_PREFIX = "shell"
    private const val DUMPsys = "dumpsys"

    fun getPackageDumpsys(packageName: String): String =
        "$SHELL_PREFIX $DUMPsys package $packageName"
}
