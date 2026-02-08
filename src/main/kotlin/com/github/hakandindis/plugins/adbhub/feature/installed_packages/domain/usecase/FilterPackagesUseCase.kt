package com.github.hakandindis.plugins.adbhub.feature.installed_packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Use case for filtering packages based on search text and applying display order.
 * Returns filtered list with user-installed (non-system) apps first, then system apps.
 */
class FilterPackagesUseCase {
    /**
     * Filters packages by search text and sorts for display: non-system apps first, then system apps.
     * @param packages List of all packages
     * @param searchText Search text to filter by (display name or package name)
     * @return Filtered and sorted list (user apps first, then system apps)
     */
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
