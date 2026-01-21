package com.github.hakandindis.plugins.adbhub.feature.packages.presentation

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * UI State for Package List feature
 */
data class PackageListUiState(
    val packages: List<ApplicationPackage> = emptyList(),
    val filteredPackages: List<ApplicationPackage> = emptyList(),
    val searchText: String = "",
    val showSystemApps: Boolean = false,
    val showUserApps: Boolean = true,
    val showDebugApps: Boolean = false,
    val selectedPackage: ApplicationPackage? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
