package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.usecase

import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.GeneralInfoMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper.PermissionMapper
import com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository.PackageDetailsRepository
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.intellij.openapi.diagnostic.Logger

class GetPackageDetailsUseCase(
    private val repository: PackageDetailsRepository
) {
    private val logger = Logger.getInstance(GetPackageDetailsUseCase::class.java)

    suspend operator fun invoke(
        packageName: String,
        deviceId: String
    ): AdbHubResult<GetPackageDetailsResult> {
        return repository.getPackageDetails(packageName, deviceId)
            .mapCatching { packageDetails ->
                val appLinks = when (val appLinksResult = repository.getAppLinks(packageName, deviceId)) {
                    is AdbHubResult.Success -> appLinksResult.data
                    is AdbHubResult.Failure -> {
                        logger.warn("App links unavailable for $packageName: ${appLinksResult.error.toUserMessage()}")
                        null
                    }
                }
                val generalInfoItems = GeneralInfoMapper.toMergedInfoItems(packageDetails)
                val appName =
                    generalInfoItems.firstOrNull { it.label == "App Name" }?.value ?: packageDetails.packageName
                GetPackageDetailsResult(
                    packageName = packageDetails.packageName,
                    appName = appName,
                    generalInfoItems = generalInfoItems,
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
