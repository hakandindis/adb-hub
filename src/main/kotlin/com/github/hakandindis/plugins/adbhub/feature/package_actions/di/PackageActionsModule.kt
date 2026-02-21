package com.github.hakandindis.plugins.adbhub.feature.package_actions.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_actions.data.repository.PackageActionsRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.usecase.*

object PackageActionsModule {

    fun createPackageActionsRepository(executor: AdbCommandExecutor): PackageActionsRepository {
        return PackageActionsRepositoryImpl(executor)
    }

    fun createLaunchAppUseCase(repository: PackageActionsRepository): LaunchAppUseCase {
        return LaunchAppUseCase(repository)
    }

    fun createForceStopUseCase(repository: PackageActionsRepository): ForceStopUseCase {
        return ForceStopUseCase(repository)
    }

    fun createClearDataUseCase(repository: PackageActionsRepository): ClearDataUseCase {
        return ClearDataUseCase(repository)
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
