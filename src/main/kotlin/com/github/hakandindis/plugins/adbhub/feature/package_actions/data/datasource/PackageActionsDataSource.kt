package com.github.hakandindis.plugins.adbhub.feature.package_actions.data.datasource


interface PackageActionsDataSource {
    suspend fun launchApp(packageName: String, deviceId: String): Result<Unit>
    suspend fun launchActivity(activityName: String, deviceId: String): Result<Unit>
    suspend fun forceStop(packageName: String, deviceId: String): Result<Unit>
    suspend fun clearData(packageName: String, deviceId: String): Result<Unit>
    suspend fun clearCache(packageName: String, deviceId: String): Result<Unit>
    suspend fun uninstall(packageName: String, deviceId: String): Result<Unit>
    suspend fun launchDeepLink(uri: String, packageName: String, deviceId: String): Result<Unit>
    suspend fun setStayAwake(enabled: Boolean, deviceId: String): Result<Unit>
    suspend fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String): Result<Unit>
}
