package com.github.hakandindis.plugins.adbhub.core.adb

/**
 * Initializes and manages ADB infrastructure
 */
class AdbInitializer(
    private val pathFinder: AdbPathFinder
) {
    private var executor: AdbCommandExecutor? = null

    /**
     * Initializes the ADB executor
     * @return true if ADB path is found and valid
     */
    fun initialize(): Boolean {
        val adbPath = pathFinder.findAdbPath()
        return if (adbPath != null && pathFinder.validateAdbPath(adbPath)) {
            executor = AdbCommandExecutor(adbPath)
            true
        } else {
            false
        }
    }

    /**
     * Checks if ADB is available
     */
    fun isAdbAvailable(): Boolean {
        return executor != null || initialize()
    }

    /**
     * Gets the ADB path
     */
    fun getAdbPath(): String? = pathFinder.findAdbPath()

    /**
     * Gets the ADB command executor
     * @return AdbCommandExecutor if initialized, null otherwise
     */
    fun getExecutor(): AdbCommandExecutor? = executor
}
