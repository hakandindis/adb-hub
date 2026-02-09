package com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

data class PackageListUiState(
    val packages: List<ApplicationPackage> = emptyList(),
    val filteredPackages: List<ApplicationPackage> = emptyList(),
    val searchText: String = "",
    val selectedPackage: ApplicationPackage? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
