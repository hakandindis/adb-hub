package com.github.hakandindis.plugins.adbhub.constants

/**
 * Package Manager (pm) command constants
 */
object PmCommands {
    private const val SHELL_PREFIX = "shell"
    private const val PM = "pm"

    /**
     * Gets APK path for a package
     * Usage: shell pm path <packageName>
     */
    fun getPackagePath(packageName: String): String {
        return "$SHELL_PREFIX $PM path $packageName"
    }

    /**
     * Clears app data
     * Usage: shell pm clear <packageName>
     */
    fun clearData(packageName: String): String {
        return "$SHELL_PREFIX $PM clear $packageName"
    }

    /**
     * Clears app cache
     * Usage: shell pm clear-cache <packageName>
     */
    fun clearCache(packageName: String): String {
        return "$SHELL_PREFIX $PM clear-cache $packageName"
    }

    /**
     * Enables a package
     * Usage: shell pm enable <packageName>
     */
    fun enablePackage(packageName: String): String {
        return "$SHELL_PREFIX $PM enable $packageName"
    }

    /**
     * Disables a package
     * Usage: shell pm disable <packageName>
     */
    fun disablePackage(packageName: String): String {
        return "$SHELL_PREFIX $PM disable $packageName"
    }

    /**
     * Grants a permission to a package
     * Usage: shell pm grant <packageName> <permission>
     */
    fun grantPermission(packageName: String, permission: String): String {
        return "$SHELL_PREFIX $PM grant $packageName $permission"
    }

    /**
     * Uninstalls a package
     * Usage: uninstall <packageName>
     * Note: uninstall command doesn't require "shell" prefix
     */
    fun uninstall(packageName: String): String {
        return "uninstall $packageName"
    }

    /**
     * Gets App Links (domain verification state) for a package.
     * Usage: shell pm get-app-links <packageName>
     * @see [Verify App Links](https://developer.android.com/training/app-links/verify-applinks)
     */
    fun getAppLinks(packageName: String): String {
        return "$SHELL_PREFIX $PM get-app-links $packageName"
    }
}
