package com.github.hakandindis.plugins.adbhub.core.adb

import java.io.File

class AdbPathFinder {

    fun findAdbPath(): String? {
        for (envVar in listOf("ANDROID_HOME", "ANDROID_SDK_ROOT")) {
            System.getenv(envVar)?.let { sdkPath ->
                val adbPath = if (isWindows()) {
                    File(sdkPath, "platform-tools/adb.exe")
                } else {
                    File(sdkPath, "platform-tools/adb")
                }
                if (adbPath.exists()) return adbPath.absolutePath
            }
        }

        val defaultPaths = getDefaultPaths()
        val found = defaultPaths.firstOrNull { path -> path.isNotBlank() && File(path).exists() }
        if (found != null) return found

        return findAdbInPath()
    }

    private fun getDefaultPaths(): List<String> {
        val osName = System.getProperty("os.name", "").lowercase()
        val userHome = System.getProperty("user.home", "")

        return when {
            osName.startsWith("windows") -> listOfNotNull(
                System.getenv("LOCALAPPDATA")?.takeIf { it.isNotBlank() }
                    ?.let { "$it/Android/Sdk/platform-tools/adb.exe" },
                System.getenv("ProgramFiles")?.takeIf { it.isNotBlank() }
                    ?.let { "$it/Android/android-sdk/platform-tools/adb.exe" },
                System.getenv("ProgramFiles(x86)")?.takeIf { it.isNotBlank() }
                    ?.let { "$it/Android/android-sdk/platform-tools/adb.exe" }
            )

            osName.contains("mac") -> listOf(
                "$userHome/Library/Android/sdk/platform-tools/adb",
                "/usr/local/bin/adb",
                "/opt/homebrew/bin/adb"
            )

            else -> listOf(
                "$userHome/Android/Sdk/platform-tools/adb",
                "/usr/bin/adb",
                "/usr/local/bin/adb"
            )
        }
    }

    private fun findAdbInPath(): String? {
        val pathEnv = System.getenv("PATH") ?: return null
        val pathSeparator = if (isWindows()) ";" else ":"
        val adbName = if (isWindows()) "adb.exe" else "adb"

        for (dir in pathEnv.split(pathSeparator)) {
            val trimmed = dir.trim().trim('"')
            if (trimmed.isEmpty()) continue
            val adbFile = File(trimmed, adbName)
            if (adbFile.exists()) return adbFile.absolutePath
        }
        return null
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name", "").startsWith("Windows", ignoreCase = true)
    }

    fun validateAdbPath(path: String): Boolean {
        val file = File(path)
        return file.exists() && (file.canExecute() || isWindows())
    }
}
