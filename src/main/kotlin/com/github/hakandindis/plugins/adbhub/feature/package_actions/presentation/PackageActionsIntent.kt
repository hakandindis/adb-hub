package com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation

/**
 * Intent actions for Package Actions feature (MVI pattern)
 */
sealed class PackageActionsIntent {
    data class LaunchApp(val packageName: String, val deviceId: String) : PackageActionsIntent()
    data class LaunchActivity(val activityName: String, val deviceId: String) : PackageActionsIntent()
    data class ForceStop(val packageName: String, val deviceId: String) : PackageActionsIntent()
    data class ClearData(val packageName: String, val deviceId: String) : PackageActionsIntent()
    data class ClearCache(val packageName: String, val deviceId: String) : PackageActionsIntent()
    data class Uninstall(val packageName: String, val deviceId: String) : PackageActionsIntent()
    data class LaunchDeepLink(val uri: String, val packageName: String, val deviceId: String) : PackageActionsIntent()
    data class StayAwake(val enabled: Boolean, val deviceId: String) : PackageActionsIntent()
    data class PackageEnabled(val packageName: String, val enabled: Boolean, val deviceId: String) :
        PackageActionsIntent()
}
