package com.github.hakandindis.plugins.adbhub.core.adb

/**
 * Initializes and manages ADB infrastructure
 */
class AdbInitializer(
    private val pathFinder: AdbPathFinder
) {
    private var executor: AdbCommandExecutor? = null

    fun initialize(): Boolean {
        val adbPath = pathFinder.findAdbPath()
        return if (adbPath != null && pathFinder.validateAdbPath(adbPath)) {
            executor = AdbCommandExecutor(adbPath)
            true
        } else {
            false
        }
    }

    fun isAdbAvailable(): Boolean {
        return executor != null || initialize()
    }

    fun getExecutor(): AdbCommandExecutor? = executor
}
