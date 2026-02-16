package com.github.hakandindis.plugins.adbhub.core.adb

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

    fun getExecutor(): AdbCommandExecutor? = executor
}
