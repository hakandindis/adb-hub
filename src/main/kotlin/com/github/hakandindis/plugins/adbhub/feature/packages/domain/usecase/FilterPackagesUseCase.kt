package com.github.hakandindis.plugins.adbhub.feature.packages.domain.usecase

import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage

/**
 * Use case for filtering packages based on search text and filters
 */
class FilterPackagesUseCase {
    /**
     * Filters packages based on search text and type filters
     * @param packages List of all packages
     * @param searchText Search text to filter by
     * @param showSystemApps Whether to show system apps
     * @param showUserApps Whether to show user-installed apps
     * @param showDebugApps Whether to show debug apps (not yet implemented)
     * @return Filtered list of packages
     */
    operator fun invoke(
        packages: List<ApplicationPackage>,
        searchText: String,
        showSystemApps: Boolean,
        showUserApps: Boolean,
        showDebugApps: Boolean
    ): List<ApplicationPackage> {
        return packages.filter { packageItem ->
            // Filter by type (System/User)
            val matchesType = (showSystemApps && packageItem.isSystemApp) ||
                    (showUserApps && !packageItem.isSystemApp)
            if (!matchesType) return@filter false

            // Filter by search text
            if (searchText.isNotBlank()) {
                val searchLower = searchText.lowercase()
                val matchesName = packageItem.displayName.lowercase().contains(searchLower)
                val matchesPackageName = packageItem.packageName.lowercase().contains(searchLower)
                if (!matchesName && !matchesPackageName) return@filter false
            }

            // TODO: Filter by debug apps when implemented
            // if (showDebugApps && !packageItem.isDebuggable) return@filter false

            true
        }
    }
}
