package com.github.hakandindis.plugins.adbhub.feature.package_actions.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_actions.data.datasource.PackageActionsDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_actions.data.datasource.PackageActionsDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.package_actions.data.repository.PackageActionsRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase.*

/**
 * Dependency injection module for Package Actions feature
 */
object PackageActionsModule {

    fun createPackageActionsDataSource(executor: AdbCommandExecutor): PackageActionsDataSource {
        return PackageActionsDataSourceImpl(executor)
    }

    fun createPackageActionsRepository(dataSource: PackageActionsDataSource): PackageActionsRepository {
        return PackageActionsRepositoryImpl(dataSource)
    }

    fun createLaunchAppUseCase(repository: PackageActionsRepository): LaunchAppUseCase {
        return LaunchAppUseCase(repository)
    }

    fun createForceStopUseCase(repository: PackageActionsRepository): ForceStopUseCase {
        return ForceStopUseCase(repository)
    }

    fun createRestartAppUseCase(
        forceStopUseCase: ForceStopUseCase,
        launchAppUseCase: LaunchAppUseCase
    ): RestartAppUseCase {
        return RestartAppUseCase(forceStopUseCase, launchAppUseCase)
    }

    fun createClearDataUseCase(repository: PackageActionsRepository): ClearDataUseCase {
        return ClearDataUseCase(repository)
    }

    fun createClearCacheUseCase(repository: PackageActionsRepository): ClearCacheUseCase {
        return ClearCacheUseCase(repository)
    }

    fun createUninstallUseCase(repository: PackageActionsRepository): UninstallUseCase {
        return UninstallUseCase(repository)
    }

    fun createLaunchDeepLinkUseCase(repository: PackageActionsRepository): LaunchDeepLinkUseCase {
        return LaunchDeepLinkUseCase(repository)
    }

    fun createSetStayAwakeUseCase(repository: PackageActionsRepository): SetStayAwakeUseCase {
        return SetStayAwakeUseCase(repository)
    }

    fun createSetPackageEnabledUseCase(repository: PackageActionsRepository): SetPackageEnabledUseCase {
        return SetPackageEnabledUseCase(repository)
    }
}
