package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay

@Composable
fun ActivitiesTab(
    activities: List<ComponentDisplay>,
    searchText: String,
    onActivitySearchChange: (String) -> Unit = {}
) {
    ComponentListTab(
        items = activities,
        searchText = searchText,
        onSearchChange = onActivitySearchChange,
        label = "Activities",
        searchPlaceholder = "Search activities (e.g. MainActivity)"
    )
}
