package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.models.CertificateInfo

/**
 * Use case for getting certificate information
 */
class GetCertificateInfoUseCase(
    private val repository: PackageDetailsRepository
) {
    /**
     * Executes the use case
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return Result containing CertificateInfo or error
     */
    suspend operator fun invoke(
        packageName: String,
        deviceId: String
    ): Result<CertificateInfo> {
        return repository.getCertificateInfo(packageName, deviceId)
    }
}
