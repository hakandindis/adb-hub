package com.github.hakandindis.plugins.adbhub.feature.package_actions.data.datasource

/**
 * Data source interface for package actions operations
 */
interface PackageActionsDataSource {
    /**
     * Launches an app by package name
     */
    suspend fun launchApp(packageName: String, deviceId: String): Result<Unit>

    /**
     * Launches a specific activity
     */
    suspend fun launchActivity(activityName: String, deviceId: String): Result<Unit>

    /**
     * Force stops an app
     */
    suspend fun forceStop(packageName: String, deviceId: String): Result<Unit>

    /**
     * Clears app data
     */
    suspend fun clearData(packageName: String, deviceId: String): Result<Unit>

    /**
     * Clears app cache
     */
    suspend fun clearCache(packageName: String, deviceId: String): Result<Unit>

    /**
     * Uninstalls an app
     */
    suspend fun uninstall(packageName: String, deviceId: String): Result<Unit>

    /**
     * Launches a deep link or URL
     */
    suspend fun launchDeepLink(deepLink: String, deviceId: String): Result<Unit>

    /**
     * Sets stay awake mode
     */
    suspend fun setStayAwake(enabled: Boolean, deviceId: String): Result<Unit>

    /**
     * Enables or disables a package
     */
    suspend fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String): Result<Unit>
}
