package com.github.hakandindis.plugins.adbhub.feature.package_actions.presentation

data class PackageActionsUiState(
    val isLaunching: Boolean = false,
    val isStopping: Boolean = false,
    val isClearingData: Boolean = false,
    val isClearingCache: Boolean = false,
    val isUninstalling: Boolean = false,
    val isSettingStayAwake: Boolean = false,
    val isSettingEnabled: Boolean = false,
    val stayAwakeEnabled: Boolean = false,
    val packageEnabled: Boolean = true,
    val error: String? = null,
    val recentUris: List<String> = emptyList()
)
