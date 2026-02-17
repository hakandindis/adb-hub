package com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository

import com.github.hakandindis.plugins.adbhub.core.result.AdbHubResult

interface PackageActionsRepository {
    suspend fun launchApp(packageName: String, deviceId: String): AdbHubResult<Unit>
    suspend fun launchActivity(activityName: String, deviceId: String): AdbHubResult<Unit>
    suspend fun forceStop(packageName: String, deviceId: String): AdbHubResult<Unit>
    suspend fun clearData(packageName: String, deviceId: String): AdbHubResult<Unit>
    suspend fun clearCache(packageName: String, deviceId: String): AdbHubResult<Unit>
    suspend fun uninstall(packageName: String, deviceId: String): AdbHubResult<Unit>
    suspend fun launchDeepLink(uri: String, deviceId: String): AdbHubResult<Unit>
    suspend fun setStayAwake(enabled: Boolean, deviceId: String): AdbHubResult<Unit>
    suspend fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String): AdbHubResult<Unit>
}
