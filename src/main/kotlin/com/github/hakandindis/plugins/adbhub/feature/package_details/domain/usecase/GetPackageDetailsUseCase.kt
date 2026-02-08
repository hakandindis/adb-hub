package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.GeneralInfoMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.PermissionMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons

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
                val appLinks = repository.getAppLinks(packageName, deviceId).getOrNull() ?: null
                GetPackageDetailsResult(
                    generalInfoItems = GeneralInfoMapper.toMergedInfoItems(packageDetails),
                    activities = packageDetails.activities.map {
                        ComponentDisplay(name = it.name, exported = it.exported, icon = AdbIcons.apps)
                    },
                    permissionSections = PermissionMapper
                        .toUiModels(packageDetails.permissionSections)
                        .sortedBy { it.sectionType.priority },
                    services = packageDetails.services.map {
                        ComponentDisplay(name = it.name, exported = it.exported, icon = AdbIcons.bolt)
                    },
                    receivers = packageDetails.receivers.map {
                        ComponentDisplay(name = it.name, exported = it.exported, icon = AdbIcons.link)
                    },
                    contentProviders = packageDetails.contentProviders.map {
                        ComponentDisplay(name = it.name, exported = it.exported, icon = AdbIcons.folder)
                    },
                    appLinks = appLinks
                )
            }
    }
}
