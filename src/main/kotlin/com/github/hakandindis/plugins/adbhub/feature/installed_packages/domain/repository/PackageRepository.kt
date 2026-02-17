package com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.repository

import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

interface PackageRepository {
    suspend fun getPackages(deviceId: String, includeSystemApps: Boolean = true): AdbHubResult<List<ApplicationPackage>>
}
