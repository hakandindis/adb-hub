package com.github.hakandindis.plugins.adbhub.toolWindow

import com.github.hakandindis.plugins.adbhub.CoroutineScopeHolder
import com.github.hakandindis.plugins.adbhub.core.adb.CommandLogger
import com.github.hakandindis.plugins.adbhub.core.di.AdbModule
import com.github.hakandindis.plugins.adbhub.feature.console_log.presentation.ConsoleLogViewModel
import com.github.hakandindis.plugins.adbhub.feature.devices.di.DeviceModule
import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.DeviceViewModel
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.di.PackageModule
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation.PackageListViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_actions.di.PackageActionsModule
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.di.PackageDetailsModule
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
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
        val coroutineScope = project.service<CoroutineScopeHolder>().createScope("AdbHubToolWindow")

        val adbInitializer = AdbModule.createAdbInitializer()
        adbInitializer.initialize()
        val executor = adbInitializer.getExecutor()

        if (executor == null) {
            toolWindow.addComposeTab(focusOnClickInside = true) {
                AdbUnavailableContent()
            }
            return
        }

        val deviceDataSource = DeviceModule.createDeviceDataSource(executor)
        val deviceRepository = DeviceModule.createDeviceRepository(deviceDataSource)
        val getDevicesUseCase = DeviceModule.createGetDevicesUseCase(deviceRepository)
        val getDeviceInfoUseCase = DeviceModule.createGetDeviceInfoUseCase(deviceRepository)
        val deviceViewModel = DeviceViewModel(
            getDevicesUseCase = getDevicesUseCase,
            getDeviceInfoUseCase = getDeviceInfoUseCase,
            coroutineScope = coroutineScope
        )

        val packageDataSource = PackageModule.createPackageDataSource(executor)
        val packageRepository = PackageModule.createPackageRepository(packageDataSource)
        val getPackagesUseCase = PackageModule.createGetPackagesUseCase(packageRepository)
        val filterPackagesUseCase = PackageModule.createFilterPackagesUseCase()
        val packageListViewModel = PackageListViewModel(
            getPackagesUseCase = getPackagesUseCase,
            filterPackagesUseCase = filterPackagesUseCase,
            coroutineScope = coroutineScope
        )

        val packageDetailsDataSource = PackageDetailsModule.createPackageDetailsDataSource(executor)
        val packageDetailsRepository = PackageDetailsModule.createPackageDetailsRepository(packageDetailsDataSource)
        val getPackageDetailsUseCase = PackageDetailsModule.createGetPackageDetailsUseCase(packageDetailsRepository)
        val packageDetailsViewModel = PackageDetailsViewModel(
            getPackageDetailsUseCase = getPackageDetailsUseCase,
            commandExecutor = executor,
            coroutineScope = coroutineScope
        )

        val packageActionsDataSource = PackageActionsModule.createPackageActionsDataSource(executor)
        val packageActionsRepository = PackageActionsModule.createPackageActionsRepository(packageActionsDataSource)
        val launchAppUseCase = PackageActionsModule.createLaunchAppUseCase(packageActionsRepository)
        val forceStopUseCase = PackageActionsModule.createForceStopUseCase(packageActionsRepository)
        val clearDataUseCase = PackageActionsModule.createClearDataUseCase(packageActionsRepository)
        val clearCacheUseCase = PackageActionsModule.createClearCacheUseCase(packageActionsRepository)
        val uninstallUseCase = PackageActionsModule.createUninstallUseCase(packageActionsRepository)
        val launchDeepLinkUseCase = PackageActionsModule.createLaunchDeepLinkUseCase(packageActionsRepository)
        val setStayAwakeUseCase = PackageActionsModule.createSetStayAwakeUseCase(packageActionsRepository)
        val setPackageEnabledUseCase = PackageActionsModule.createSetPackageEnabledUseCase(packageActionsRepository)
        val packageActionsViewModel = PackageActionsViewModel(
            launchAppUseCase = launchAppUseCase,
            forceStopUseCase = forceStopUseCase,
            clearDataUseCase = clearDataUseCase,
            clearCacheUseCase = clearCacheUseCase,
            uninstallUseCase = uninstallUseCase,
            launchDeepLinkUseCase = launchDeepLinkUseCase,
            setStayAwakeUseCase = setStayAwakeUseCase,
            setPackageEnabledUseCase = setPackageEnabledUseCase,
            coroutineScope = coroutineScope
        )

        val consoleLogViewModel = ConsoleLogViewModel(
            commandLogger = CommandLogger.getInstance(),
            coroutineScope = coroutineScope
        )

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
