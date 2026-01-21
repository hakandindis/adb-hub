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
     * Starts an activity with intent action and data
     * Usage: shell am start -a <action> -d <data>
     */
    fun startActivityWithAction(action: String, data: String): String {
        return "$SHELL_PREFIX $AM $START -a $action -d \"$data\""
    }

    /**
     * Force stops an app
     * Usage: shell am force-stop <packageName>
     */
    fun forceStop(packageName: String): String {
        return "$SHELL_PREFIX $AM force-stop $packageName"
    }
}
