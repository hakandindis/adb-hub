package com.github.hakandindis.plugins.adbhub.feature.package_details.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.PackageDetailsDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.PackageDetailsDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.repository.PackageDetailsRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetPackageDetailsUseCase

/**
 * Dependency injection module for Package Details feature
 */
object PackageDetailsModule {
    /**
     * Creates PackageDetailsViewModel dependencies
     */
    fun createPackageDetailsDataSource(executor: AdbCommandExecutor): PackageDetailsDataSource {
        return PackageDetailsDataSourceImpl(executor)
    }

    fun createPackageDetailsRepository(
        packageDetailsDataSource: PackageDetailsDataSource
    ): PackageDetailsRepository {
        return PackageDetailsRepositoryImpl(packageDetailsDataSource)
    }

    fun createGetPackageDetailsUseCase(repository: PackageDetailsRepository): GetPackageDetailsUseCase {
        return GetPackageDetailsUseCase(repository)
    }
}
