package com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Use case for filtering packages based on search text and filters
 */
class FilterPackagesUseCase {
    /**
     * Filters packages based on search text and type filters
     * @param packages List of all packages
     * @param searchText Search text to filter by
     * @return Filtered list of packages
     */
    operator fun invoke(
        packages: List<ApplicationPackage>,
        searchText: String,
    ): List<ApplicationPackage> {
        return packages.filter { packageItem ->

            if (searchText.isNotBlank()) {
                val searchLower = searchText.lowercase()
                val matchesName = packageItem.displayName.lowercase().contains(searchLower)
                val matchesPackageName = packageItem.packageName.lowercase().contains(searchLower)
                if (!matchesName && !matchesPackageName) return@filter false
            }

            true
        }
    }
}
