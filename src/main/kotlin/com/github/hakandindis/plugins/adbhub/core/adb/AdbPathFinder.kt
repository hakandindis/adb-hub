package com.github.hakandindis.plugins.adbhub.core.adb

import java.io.File

/**
 * Service for finding ADB executable path
 */
class AdbPathFinder {

    /**
     * Finds the ADB executable path
     * @return ADB path or null if not found
     */
    fun findAdbPath(): String? {
        System.getenv("ANDROID_HOME")?.let {
            val adb = File(it, "platform-tools/adb")
            if (adb.exists()) {
                return if (isWindows()) "$adb.exe" else adb.absolutePath
            }
        }

        System.getenv("ANDROID_SDK_ROOT")?.let {
            val adb = File(it, "platform-tools/adb")
            if (adb.exists()) {
                return if (isWindows()) "$adb.exe" else adb.absolutePath
            }
        }

        val defaultPaths = getDefaultPaths()
        return defaultPaths.firstOrNull { File(it).exists() }
    }

    private fun getDefaultPaths(): List<String> {
        val osName = System.getProperty("os.name")

        return when {
            osName.startsWith("Windows") -> listOf(
                "${System.getenv("LOCALAPPDATA")}/Android/Sdk/platform-tools/adb.exe",
                "${System.getenv("ProgramFiles")}/Android/android-sdk/platform-tools/adb.exe",
                "${System.getenv("ProgramFiles(x86)")}/Android/android-sdk/platform-tools/adb.exe"
            )

            osName == "Mac OS X" -> listOf(
                "${System.getProperty("user.home")}/Library/Android/sdk/platform-tools/adb",
                "/usr/local/bin/adb",
                "/opt/homebrew/bin/adb"
            )

            else -> listOf(
                "${System.getProperty("user.home")}/Android/Sdk/platform-tools/adb",
                "/usr/bin/adb",
                "/usr/local/bin/adb"
            )
        }
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name").startsWith("Windows")
    }

    fun validateAdbPath(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.canExecute()
    }
}
