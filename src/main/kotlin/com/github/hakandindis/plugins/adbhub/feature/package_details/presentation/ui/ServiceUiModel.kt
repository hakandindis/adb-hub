package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

/**
 * UI model for displaying service information in package details
 */
data class ServiceUiModel(
    val name: String,
    val shortName: String,
    val exported: Boolean,
    val enabled: Boolean = true
)
