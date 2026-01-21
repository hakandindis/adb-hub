package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

/**
 * UI model for displaying activity information
 */
data class ActivityUiModel(
    val name: String,
    val shortName: String,
    val fullName: String,
    val isLauncher: Boolean,
    val isExported: Boolean,
    val intentFilters: List<IntentFilterUiModel>
)

/**
 * UI model for intent filter information
 */
data class IntentFilterUiModel(
    val actions: List<String>,
    val categories: List<String>,
    val data: List<String>
)
