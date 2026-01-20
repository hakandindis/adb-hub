package com.github.hakandindis.plugins.adbhub.core.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbInitializer
import com.github.hakandindis.plugins.adbhub.core.adb.AdbPathFinder

/**
 * Dependency injection module for ADB core components
 * Since IntelliJ Platform doesn't have a built-in DI framework,
 * we use manual dependency injection through this module
 */
object AdbModule {
    /**
     * Creates an AdbInitializer instance
     */
    fun createAdbInitializer(): AdbInitializer {
        val pathFinder = AdbPathFinder()
        return AdbInitializer(pathFinder)
    }
}
