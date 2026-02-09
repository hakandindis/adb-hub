package com.github.hakandindis.plugins.adbhub.feature.package_actions.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation.PackageActionsViewModel
import com.github.hakandindis.plugins.adbhub.service.RecentDeepLinksService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class PackageActionsViewModelFactory(
    @Suppress("unused") private val project: Project,
    private val coroutineScope: CoroutineScope
) {

    fun create(executor: AdbCommandExecutor): PackageActionsViewModel {
        val packageActionsRepository = PackageActionsModule.createPackageActionsRepository(executor)
        val launchAppUseCase = PackageActionsModule.createLaunchAppUseCase(packageActionsRepository)
        val forceStopUseCase = PackageActionsModule.createForceStopUseCase(packageActionsRepository)
        val clearDataUseCase = PackageActionsModule.createClearDataUseCase(packageActionsRepository)
        val clearCacheUseCase = PackageActionsModule.createClearCacheUseCase(packageActionsRepository)
        val uninstallUseCase = PackageActionsModule.createUninstallUseCase(packageActionsRepository)
        val launchDeepLinkUseCase = PackageActionsModule.createLaunchDeepLinkUseCase(packageActionsRepository)
        val setStayAwakeUseCase = PackageActionsModule.createSetStayAwakeUseCase(packageActionsRepository)
        val setPackageEnabledUseCase = PackageActionsModule.createSetPackageEnabledUseCase(packageActionsRepository)
        val recentDeepLinksService = ApplicationManager.getApplication().service<RecentDeepLinksService>()
        return PackageActionsViewModel(
            launchAppUseCase = launchAppUseCase,
            forceStopUseCase = forceStopUseCase,
            clearDataUseCase = clearDataUseCase,
            clearCacheUseCase = clearCacheUseCase,
            uninstallUseCase = uninstallUseCase,
            launchDeepLinkUseCase = launchDeepLinkUseCase,
            setStayAwakeUseCase = setStayAwakeUseCase,
            setPackageEnabledUseCase = setPackageEnabledUseCase,
            recentDeepLinksService = recentDeepLinksService,
            coroutineScope = coroutineScope
        )
    }
}
