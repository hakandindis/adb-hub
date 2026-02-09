package com.github.hakandindis.plugins.adbhub.constants

object GetpropCommands {
    private const val SHELL_PREFIX = "shell"
    private const val GETPROP = "getprop"

    fun getProperty(property: String): String =
        "$SHELL_PREFIX $GETPROP $property"
}
