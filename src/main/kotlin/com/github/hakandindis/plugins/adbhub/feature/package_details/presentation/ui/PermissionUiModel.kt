package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

import com.github.hakandindis.plugins.adbhub.models.PermissionStatus

/**
 * UI model for displaying permission information
 */
data class PermissionUiModel(
    val name: String,
    val status: PermissionStatus
)
