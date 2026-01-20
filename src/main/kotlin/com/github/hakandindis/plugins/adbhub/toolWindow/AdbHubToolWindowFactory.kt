package com.github.hakandindis.plugins.adbhub.toolWindow

import com.github.hakandindis.plugins.adbhub.CoroutineScopeHolder
import com.github.hakandindis.plugins.adbhub.core.di.AdbModule
import com.github.hakandindis.plugins.adbhub.feature.device.di.DeviceModule
import com.github.hakandindis.plugins.adbhub.feature.device.presentation.DeviceViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_actions.di.PackageActionsModule
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.di.PackageDetailsModule
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
import com.github.hakandindis.plugins.adbhub.feature.packages.di.PackageModule
import com.github.hakandindis.plugins.adbhub.feature.packages.presentation.PackageListViewModel
import com.github.hakandindis.plugins.adbhub.ui.AdbToolContent
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
        // Create coroutine scope for the tool window
        val coroutineScope = project.service<CoroutineScopeHolder>()
            .createScope("AdbToolWindow")

        // Initialize ADB
        val adbInitializer = AdbModule.createAdbInitializer()
        adbInitializer.initialize()
        val executor = adbInitializer.getExecutor()

        // Create DeviceViewModel
        val deviceViewModel = if (executor != null) {
            val deviceDataSource = DeviceModule.createDeviceDataSource(executor)
            if (deviceDataSource != null) {
                val deviceRepository = DeviceModule.createDeviceRepository(deviceDataSource)
                val getDevicesUseCase = DeviceModule.createGetDevicesUseCase(deviceRepository)
                val getDeviceInfoUseCase = DeviceModule.createGetDeviceInfoUseCase(deviceRepository)
                DeviceViewModel(
                    getDevicesUseCase = getDevicesUseCase,
                    getDeviceInfoUseCase = getDeviceInfoUseCase,
                    coroutineScope = coroutineScope
                )
            } else {
                null
            }
        } else {
            null
        }

        // Create PackageListViewModel
        val packageListViewModel = if (executor != null) {
            val packageDataSource = PackageModule.createPackageDataSource(executor)
            if (packageDataSource != null) {
                val packageRepository = PackageModule.createPackageRepository(packageDataSource)
                val getPackagesUseCase = PackageModule.createGetPackagesUseCase(packageRepository)
                val filterPackagesUseCase = PackageModule.createFilterPackagesUseCase()
                PackageListViewModel(
                    getPackagesUseCase = getPackagesUseCase,
                    filterPackagesUseCase = filterPackagesUseCase,
                    coroutineScope = coroutineScope
                )
            } else {
                null
            }
        } else {
            null
        }

        // Create PackageDetailsViewModel
        val packageDetailsViewModel = if (executor != null) {
            val packageDetailsDataSource = PackageDetailsModule.createPackageDetailsDataSource(executor)
            val certificateDataSource = PackageDetailsModule.createCertificateDataSource(executor)
            if (packageDetailsDataSource != null && certificateDataSource != null) {
                val packageDetailsRepository = PackageDetailsModule.createPackageDetailsRepository(
                    packageDetailsDataSource,
                    certificateDataSource
                )
                val getPackageDetailsUseCase =
                    PackageDetailsModule.createGetPackageDetailsUseCase(packageDetailsRepository)
                val getCertificateInfoUseCase =
                    PackageDetailsModule.createGetCertificateInfoUseCase(packageDetailsRepository)
                PackageDetailsViewModel(
                    getPackageDetailsUseCase = getPackageDetailsUseCase,
                    getCertificateInfoUseCase = getCertificateInfoUseCase,
                    commandExecutor = executor,
                    coroutineScope = coroutineScope
                )
            } else {
                null
            }
        } else {
            null
        }

        // Create PackageActionsViewModel
        val packageActionsViewModel = if (executor != null) {
            val packageActionsDataSource = PackageActionsModule.createPackageActionsDataSource(executor)
            if (packageActionsDataSource != null) {
                val packageActionsRepository =
                    PackageActionsModule.createPackageActionsRepository(packageActionsDataSource)
                val launchAppUseCase = PackageActionsModule.createLaunchAppUseCase(packageActionsRepository)
                val forceStopUseCase = PackageActionsModule.createForceStopUseCase(packageActionsRepository)
                val restartAppUseCase = PackageActionsModule.createRestartAppUseCase(forceStopUseCase, launchAppUseCase)
                val clearDataUseCase = PackageActionsModule.createClearDataUseCase(packageActionsRepository)
                val clearCacheUseCase = PackageActionsModule.createClearCacheUseCase(packageActionsRepository)
                val uninstallUseCase = PackageActionsModule.createUninstallUseCase(packageActionsRepository)
                val launchDeepLinkUseCase = PackageActionsModule.createLaunchDeepLinkUseCase(packageActionsRepository)
                val setStayAwakeUseCase = PackageActionsModule.createSetStayAwakeUseCase(packageActionsRepository)
                val setPackageEnabledUseCase =
                    PackageActionsModule.createSetPackageEnabledUseCase(packageActionsRepository)
                PackageActionsViewModel(
                    launchAppUseCase = launchAppUseCase,
                    forceStopUseCase = forceStopUseCase,
                    restartAppUseCase = restartAppUseCase,
                    clearDataUseCase = clearDataUseCase,
                    clearCacheUseCase = clearCacheUseCase,
                    uninstallUseCase = uninstallUseCase,
                    launchDeepLinkUseCase = launchDeepLinkUseCase,
                    setStayAwakeUseCase = setStayAwakeUseCase,
                    setPackageEnabledUseCase = setPackageEnabledUseCase,
                    coroutineScope = coroutineScope
                )
            } else {
                null
            }
        } else {
            null
        }

        // Register for disposal
        deviceViewModel?.let { Disposer.register(toolWindow.disposable, it) }
        packageListViewModel?.let { Disposer.register(toolWindow.disposable, it) }
        packageDetailsViewModel?.let { Disposer.register(toolWindow.disposable, it) }
        packageActionsViewModel?.let { Disposer.register(toolWindow.disposable, it) }

        // Add Compose tab
        toolWindow.addComposeTab(focusOnClickInside = true) {
            AdbToolContent(
                adbInitializer = adbInitializer,
                deviceViewModel = deviceViewModel,
                packageListViewModel = packageListViewModel,
                packageDetailsViewModel = packageDetailsViewModel,
                packageActionsViewModel = packageActionsViewModel
            )
        }
    }
}
