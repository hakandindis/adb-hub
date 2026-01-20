package com.github.hakandindis.plugins.adbhub.constants

/**
 * Settings command constants
 */
object SettingsCommands {
    private const val SHELL_PREFIX = "shell"
    private const val SETTINGS = "settings"
    private const val PUT = "put"
    private const val GLOBAL = "global"

    /**
     * Sets stay awake while plugged in setting
     * Usage: shell settings put global stay_on_while_plugged_in <value>
     * Value: 0 = disabled, 1 = enabled
     */
    fun setStayAwake(enabled: Boolean): String {
        val value = if (enabled) "1" else "0"
        return "$SHELL_PREFIX $SETTINGS $PUT $GLOBAL stay_on_while_plugged_in $value"
    }
}
