package com.github.hakandindis.plugins.adbhub.feature.package_details.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.CertificateDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.CertificateDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.PackageDetailsDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource.PackageDetailsDataSourceImpl
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.repository.PackageDetailsRepositoryImpl
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetCertificateInfoUseCase
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase.GetPackageDetailsUseCase

/**
 * Dependency injection module for Package Details feature
 */
object PackageDetailsModule {
    /**
     * Creates PackageDetailsViewModel dependencies
     */
    fun createPackageDetailsDataSource(executor: AdbCommandExecutor?): PackageDetailsDataSource? {
        return executor?.let { PackageDetailsDataSourceImpl(it) }
    }

    fun createCertificateDataSource(executor: AdbCommandExecutor?): CertificateDataSource? {
        return executor?.let { CertificateDataSourceImpl(it) }
    }

    fun createPackageDetailsRepository(
        packageDetailsDataSource: PackageDetailsDataSource,
        certificateDataSource: CertificateDataSource
    ): PackageDetailsRepository {
        return PackageDetailsRepositoryImpl(packageDetailsDataSource, certificateDataSource)
    }

    fun createGetPackageDetailsUseCase(repository: PackageDetailsRepository): GetPackageDetailsUseCase {
        return GetPackageDetailsUseCase(repository)
    }

    fun createGetCertificateInfoUseCase(repository: PackageDetailsRepository): GetCertificateInfoUseCase {
        return GetCertificateInfoUseCase(repository)
    }
}
