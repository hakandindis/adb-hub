package com.github.hakandindis.plugins.adbhub.constants

/**
 * Activity Manager (am) command constants
 */
object AmCommands {
    private const val SHELL_PREFIX = "shell"
    private const val AM = "am"
    private const val START = "start"

    /**
     * Starts an activity by component name
     * Usage: shell am start -n <componentName>
     */
    fun startActivity(componentName: String): String {
        return "$SHELL_PREFIX $AM $START -n $componentName"
    }

    /**
     * Starts an activity with intent action and data.
     * When [packageName] is provided, adds -p to target the given package (e.g. for deep links).
     * Usage: shell am start -a <action> -d <data> [-p <packageName>]
     */
    fun startActivityWithAction(action: String, data: String, packageName: String? = null): String {
        val base = "$SHELL_PREFIX $AM $START -a $action -d \"$data\""
        return if (packageName != null) "$base -p $packageName" else base
    }

    /**
     * Force stops an app
     * Usage: shell am force-stop <packageName>
     */
    fun forceStop(packageName: String): String {
        return "$SHELL_PREFIX $AM force-stop $packageName"
    }
}
