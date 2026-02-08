package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

/**
 * UI model for displaying content provider information in package details
 */
data class ContentProviderUiModel(
    val name: String,
    val shortName: String,
    val exported: Boolean,
    val enabled: Boolean = true
)
