package com.github.hakandindis.plugins.adbhub.constants

object MonkeyCommands {
    private const val SHELL_PREFIX = "shell"
    private const val MONKEY = "monkey"
    private const val PACKAGE_FLAG = "-p"
    private const val CATEGORY_FLAG = "-c"

    fun launchApp(packageName: String): String =
        "$SHELL_PREFIX $MONKEY $PACKAGE_FLAG $packageName $CATEGORY_FLAG ${IntentConstants.Categories.LAUNCHER} 1"
}
