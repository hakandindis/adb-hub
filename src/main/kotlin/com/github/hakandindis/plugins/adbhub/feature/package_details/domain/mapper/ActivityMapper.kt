package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.constants.IntentConstants
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ActivityUiModel
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.IntentFilterUiModel
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Mapper for converting ActivityInfo to UI models
 */
object ActivityMapper {
    /**
     * Converts ActivityInfo to ActivityUiModel
     */
    fun toUiModel(activity: PackageDetails.ActivityInfo): ActivityUiModel {
        return ActivityUiModel(
            name = activity.name,
            shortName = extractShortName(activity.name),
            fullName = activity.name,
            isLauncher = isLauncherActivity(activity),
            isExported = activity.exported,
            intentFilters = activity.intentFilters.map { filter ->
                IntentFilterUiModel(
                    actions = filter.actions,
                    categories = filter.categories,
                    data = filter.data
                )
            }
        )
    }

    /**
     * Extracts short name from full activity name (package/ActivityName -> ActivityName)
     */
    private fun extractShortName(fullName: String): String {
        return fullName.substringAfterLast("/")
    }

    /**
     * Determines if an activity is a launcher activity
     * A launcher activity must have an intent filter with both MAIN action and LAUNCHER category
     */
    fun isLauncherActivity(activity: PackageDetails.ActivityInfo): Boolean {
        return activity.intentFilters.any { filter ->
            val hasMainAction = filter.actions.any { action ->
                IntentConstants.isMainAction(action)
            }
            val hasLauncherCategory = filter.categories.any { category ->
                IntentConstants.isLauncherCategory(category)
            }
            hasMainAction && hasLauncherCategory
        }
    }
}
