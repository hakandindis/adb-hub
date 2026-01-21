package com.github.hakandindis.plugins.adbhub.constants

/**
 * Monkey command constants (for launching apps)
 */
object MonkeyCommands {
    private const val SHELL_PREFIX = "shell"
    private const val MONKEY = "monkey"
    private const val PACKAGE_FLAG = "-p"
    private const val CATEGORY_FLAG = "-c"

    /**
     * Launches an app using monkey command
     * Usage: shell monkey -p <packageName> -c android.intent.category.LAUNCHER 1
     */
    fun launchApp(packageName: String): String {
        return "$SHELL_PREFIX $MONKEY $PACKAGE_FLAG $packageName $CATEGORY_FLAG ${IntentConstants.Categories.LAUNCHER} 1"
    }
}
