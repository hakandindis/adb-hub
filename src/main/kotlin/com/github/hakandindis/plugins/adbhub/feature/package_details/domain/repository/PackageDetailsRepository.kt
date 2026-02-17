package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.repository

import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.models.AppLinksInfo
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

interface PackageDetailsRepository {
    suspend fun getPackageDetails(packageName: String, deviceId: String): AdbHubResult<PackageDetails>
    suspend fun getAppLinks(packageName: String, deviceId: String): AdbHubResult<AppLinksInfo?>
}
