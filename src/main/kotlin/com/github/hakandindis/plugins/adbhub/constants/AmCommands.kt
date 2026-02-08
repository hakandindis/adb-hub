package com.github.hakandindis.plugins.adbhub.constants

object AmCommands {
    private const val SHELL_PREFIX = "shell"
    private const val AM = "am"
    private const val START = "start"

    fun startActivity(componentName: String): String =
        "$SHELL_PREFIX $AM $START -n $componentName"

    fun startActivityWithAction(action: String, data: String, packageName: String? = null): String {
        val base = "$SHELL_PREFIX $AM $START -a $action -d \"$data\""
        return if (packageName != null) "$base -p $packageName" else base
    }

    fun forceStop(packageName: String): String =
        "$SHELL_PREFIX $AM force-stop $packageName"
}
