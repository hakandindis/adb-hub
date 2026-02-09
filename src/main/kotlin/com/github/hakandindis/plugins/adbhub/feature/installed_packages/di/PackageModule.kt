package com.github.hakandindis.plugins.adbhub.feature.installed_packages.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.data.repository.PackageRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase.FilterPackagesUseCase
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase.GetPackagesUseCase

object PackageModule {

    fun createPackageRepository(executor: AdbCommandExecutor): PackageRepository {
        return PackageRepositoryImpl(executor)
    }

    fun createGetPackagesUseCase(repository: PackageRepository): GetPackagesUseCase {
        return GetPackagesUseCase(repository)
    }

    fun createFilterPackagesUseCase(): FilterPackagesUseCase {
        return FilterPackagesUseCase()
    }
}
