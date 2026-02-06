package com.github.hakandindis.plugins.adbhub.feature.console_log.di

import com.github.hakandindis.plugins.adbhub.core.adb.CommandLogger
import com.github.hakandindis.plugins.adbhub.feature.console_log.presentation.ConsoleLogViewModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class ConsoleLogViewModelFactory(
    @Suppress("unused") private val project: Project,
    private val coroutineScope: CoroutineScope
) {

    fun create(): ConsoleLogViewModel {
        return ConsoleLogViewModel(
            commandLogger = CommandLogger.getInstance(),
            coroutineScope = coroutineScope
        )
    }
}
