package com.github.hakandindis.plugins.adbhub.feature.package_details.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.repository.PackageDetailsRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetPackageDetailsUseCase

object PackageDetailsModule {

    fun createPackageDetailsRepository(executor: AdbCommandExecutor): PackageDetailsRepository {
        return PackageDetailsRepositoryImpl(executor)
    }

    fun createGetPackageDetailsUseCase(repository: PackageDetailsRepository): GetPackageDetailsUseCase {
        return GetPackageDetailsUseCase(repository)
    }
}
