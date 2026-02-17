package com.github.hakandindis.plugins.adbhub.core.coroutine

import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private val logger = Logger.getInstance("AdbHub.CoroutineScope")

val AdbHubCoroutineExceptionHandler: CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        logger.error("Unhandled coroutine exception in ADB Hub", throwable)
    }

fun CoroutineScope.safeLaunch(
    block: suspend CoroutineScope.() -> Unit
): Job = launch(context = AdbHubCoroutineExceptionHandler, block = block)
