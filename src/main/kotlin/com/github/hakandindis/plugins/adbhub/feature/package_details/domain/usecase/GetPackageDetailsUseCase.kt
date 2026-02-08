package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.*
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository

/**
 * Use case for getting package details.
 * Fetches raw data from the repository and maps it to view-ready UI models.
 */
class GetPackageDetailsUseCase(
    private val repository: PackageDetailsRepository
) {
    /**
     * Executes the use case.
     * @param packageName Package name
     * @param deviceId Device ID (serial number)
     * @return Result containing view-ready [GetPackageDetailsResult] or error
     */
    suspend operator fun invoke(
        packageName: String,
        deviceId: String
    ): Result<GetPackageDetailsResult> {
        return repository.getPackageDetails(packageName, deviceId)
            .mapCatching { packageDetails ->
                GetPackageDetailsResult(
                    generalInfoItems = GeneralInfoMapper.toMergedInfoItems(packageDetails),
                    activities = packageDetails.activities.map { ActivityMapper.toUiModel(it) },
                    permissionSections = PermissionMapper.toUiModels(packageDetails.permissionSections),
                    services = ServiceMapper.toUiModels(packageDetails.services),
                    receivers = ReceiverMapper.toUiModels(packageDetails.receivers),
                    contentProviders = ContentProviderMapper.toUiModels(packageDetails.contentProviders)
                )
            }
    }
}
