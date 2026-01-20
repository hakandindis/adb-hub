package com.github.hakandindis.plugins.adbhub.constants

/**
 * Getprop command constants
 */
object GetpropCommands {
    private const val SHELL_PREFIX = "shell"
    private const val GETPROP = "getprop"

    /**
     * Gets a device property value
     * Usage: shell getprop <property>
     */
    fun getProperty(property: String): String {
        return "$SHELL_PREFIX $GETPROP $property"
    }
}
