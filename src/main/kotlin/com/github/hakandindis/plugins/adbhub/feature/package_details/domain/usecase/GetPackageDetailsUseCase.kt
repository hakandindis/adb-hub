package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Use case for getting package details
 */
class GetPackageDetailsUseCase(
    private val repository: PackageDetailsRepository
) {
    /**
     * Executes the use case
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return Result containing PackageDetails or error
     */
    suspend operator fun invoke(
        packageName: String,
        deviceId: String
    ): Result<PackageDetails> {
        return repository.getPackageDetails(packageName, deviceId)
    }
}
