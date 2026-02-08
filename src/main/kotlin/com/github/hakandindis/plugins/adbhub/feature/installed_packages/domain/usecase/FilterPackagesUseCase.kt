package com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

class FilterPackagesUseCase {

    operator fun invoke(
        packages: List<ApplicationPackage>,
        searchText: String,
    ): List<ApplicationPackage> {
        val filtered = packages.filter { packageItem ->
            if (searchText.isNotBlank()) {
                val searchLower = searchText.lowercase()
                val matchesName = packageItem.displayName.lowercase().contains(searchLower)
                val matchesPackageName = packageItem.packageName.lowercase().contains(searchLower)
                matchesName || matchesPackageName
            } else {
                true
            }
        }
        return filtered.sortedBy { it.isSystemApp }
    }
}
