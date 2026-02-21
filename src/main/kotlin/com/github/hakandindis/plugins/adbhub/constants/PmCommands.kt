package com.github.hakandindis.plugins.adbhub.constants

object PmCommands {
    private const val SHELL_PREFIX = "shell"
    private const val PM = "pm"

    fun clearData(packageName: String): String = "$SHELL_PREFIX $PM clear $packageName"
    fun enablePackage(packageName: String): String = "$SHELL_PREFIX $PM enable $packageName"
    fun disablePackage(packageName: String): String = "$SHELL_PREFIX $PM disable $packageName"
    fun grantPermission(packageName: String, permission: String): String =
        "$SHELL_PREFIX $PM grant $packageName $permission"

    fun uninstall(packageName: String): String = "uninstall $packageName"
    fun getAppLinks(packageName: String): String = "$SHELL_PREFIX $PM get-app-links $packageName"
}
