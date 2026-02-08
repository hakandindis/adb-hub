package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

/**
 * UI model for displaying receiver information in package details
 */
data class ReceiverUiModel(
    val name: String,
    val shortName: String,
    val exported: Boolean,
    val enabled: Boolean = true
)
