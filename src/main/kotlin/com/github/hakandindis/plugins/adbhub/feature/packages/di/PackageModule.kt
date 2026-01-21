package com.github.hakandindis.plugins.adbhub.feature.packages.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.packages.data.datasource.PackageDataSource
import com.github.hakandindis.plugins.adbhub.feature.packages.data.datasource.PackageDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.packages.data.repository.PackageRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.packages.domain.repository.PackageRepository
import com.github.hakandindis.plugins.adbhub.feature.packages.domain.usecase.FilterPackagesUseCase
import com.github.hakandindis.plugins.adbhub.feature.packages.domain.usecase.GetPackagesUseCase

/**
 * Dependency injection module for Package List feature
 */
object PackageModule {

    fun createPackageDataSource(executor: AdbCommandExecutor?): PackageDataSource? {
        return executor?.let { PackageDataSourceImpl(it) }
    }

    fun createPackageRepository(dataSource: PackageDataSource): PackageRepository {
        return PackageRepositoryImpl(dataSource)
    }

    fun createGetPackagesUseCase(repository: PackageRepository): GetPackagesUseCase {
        return GetPackagesUseCase(repository)
    }

    fun createFilterPackagesUseCase(): FilterPackagesUseCase {
        return FilterPackagesUseCase()
    }
}
