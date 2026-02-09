package com.github.hakandindis.plugins.adbhub.constants

object AdbCommands {
    const val DEVICES = "devices -l"
    const val LIST_PACKAGES = "shell pm list packages -f"
    const val LIST_PACKAGES_USER = "shell pm list packages -f -3"
    const val LIST_PACKAGES_NO_PATH = "shell pm list packages"
    const val LIST_PACKAGES_USER_NO_PATH = "shell pm list packages -3"
}
