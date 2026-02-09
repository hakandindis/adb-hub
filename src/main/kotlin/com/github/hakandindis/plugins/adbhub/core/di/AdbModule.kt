package com.github.hakandindis.plugins.adbhub.core.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbInitializer
import com.github.hakandindis.plugins.adbhub.core.adb.AdbPathFinder

object AdbModule {

    fun createAdbInitializer(): AdbInitializer {
        val pathFinder = AdbPathFinder()
        return AdbInitializer(pathFinder)
    }
}
