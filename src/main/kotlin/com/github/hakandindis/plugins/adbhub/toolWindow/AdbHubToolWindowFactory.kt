package com.github.hakandindis.plugins.adbhub.toolWindow

import com.github.hakandindis.plugins.adbhub.core.di.AdbModule
import com.github.hakandindis.plugins.adbhub.feature.console_log.di.ConsoleLogViewModelFactory
import com.github.hakandindis.plugins.adbhub.feature.devices.di.DeviceViewModelFactory
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.di.PackageListViewModelFactory
import com.github.hakandindis.plugins.adbhub.feature.package_actions.di.PackageActionsViewModelFactory
import com.github.hakandindis.plugins.adbhub.feature.package_details.di.PackageDetailsViewModelFactory
import com.github.hakandindis.plugins.adbhub.ui.AdbToolContent
import com.github.hakandindis.plugins.adbhub.ui.AdbUnavailableContent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.jetbrains.jewel.bridge.addComposeTab

/**
 * Factory for creating ADB Tool Window
 */
class AdbHubToolWindowFactory : ToolWindowFactory, DumbAware {

    /**
     * Tool window should be available even when no project is open
     */
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val adbInitializer = AdbModule.createAdbInitializer()
        adbInitializer.initialize()
        val executor = adbInitializer.getExecutor()

        if (executor == null) {
            toolWindow.addComposeTab(focusOnClickInside = true) {
                AdbUnavailableContent()
            }
            return
        }

        val deviceViewModel = project.service<DeviceViewModelFactory>().create(executor)
        val packageListViewModel = project.service<PackageListViewModelFactory>().create(executor)
        val packageDetailsViewModel = project.service<PackageDetailsViewModelFactory>().create(executor)
        val packageActionsViewModel = project.service<PackageActionsViewModelFactory>().create(executor)
        val consoleLogViewModel = project.service<ConsoleLogViewModelFactory>().create()

        Disposer.register(toolWindow.disposable, deviceViewModel)
        Disposer.register(toolWindow.disposable, packageListViewModel)
        Disposer.register(toolWindow.disposable, packageDetailsViewModel)
        Disposer.register(toolWindow.disposable, packageActionsViewModel)
        Disposer.register(toolWindow.disposable, consoleLogViewModel)

        toolWindow.addComposeTab(focusOnClickInside = true) {
            AdbToolContent(
                adbInitializer = adbInitializer,
                deviceViewModel = deviceViewModel,
                packageListViewModel = packageListViewModel,
                packageDetailsViewModel = packageDetailsViewModel,
                packageActionsViewModel = packageActionsViewModel,
                consoleLogViewModel = consoleLogViewModel
            )
        }
    }
}
