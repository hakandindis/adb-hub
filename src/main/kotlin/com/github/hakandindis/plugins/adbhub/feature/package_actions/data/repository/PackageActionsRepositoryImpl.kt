package com.github.hakandindis.plugins.adbhub.feature.package_actions.data.repository

import com.github.hakandindis.plugins.adbhub.feature.package_actions.data.datasource.PackageActionsDataSource
import com.github.hakandindis.plugins.adbhub.feature.package_actions.domain.repository.PackageActionsRepository

/**
 * Implementation of PackageActionsRepository
 */
class PackageActionsRepositoryImpl(
    private val dataSource: PackageActionsDataSource
) : PackageActionsRepository {

    override suspend fun launchApp(packageName: String, deviceId: String): Result<Unit> {
        return dataSource.launchApp(packageName, deviceId)
    }

    override suspend fun launchActivity(activityName: String, deviceId: String): Result<Unit> {
        return dataSource.launchActivity(activityName, deviceId)
    }

    override suspend fun forceStop(packageName: String, deviceId: String): Result<Unit> {
        return dataSource.forceStop(packageName, deviceId)
    }

    override suspend fun clearData(packageName: String, deviceId: String): Result<Unit> {
        return dataSource.clearData(packageName, deviceId)
    }

    override suspend fun clearCache(packageName: String, deviceId: String): Result<Unit> {
        return dataSource.clearCache(packageName, deviceId)
    }

    override suspend fun uninstall(packageName: String, deviceId: String): Result<Unit> {
        return dataSource.uninstall(packageName, deviceId)
    }

    override suspend fun launchDeepLink(uri: String, packageName: String, deviceId: String): Result<Unit> {
        return dataSource.launchDeepLink(uri, packageName, deviceId)
    }

    override suspend fun setStayAwake(enabled: Boolean, deviceId: String): Result<Unit> {
        return dataSource.setStayAwake(enabled, deviceId)
    }

    override suspend fun setPackageEnabled(packageName: String, enabled: Boolean, deviceId: String): Result<Unit> {
        return dataSource.setPackageEnabled(packageName, enabled, deviceId)
    }
}
