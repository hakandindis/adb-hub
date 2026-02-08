package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay

@Composable
fun ServicesTab(
    services: List<ComponentDisplay>,
    searchText: String,
    onServiceSearchChange: (String) -> Unit
) {
    ComponentListTab(
        items = services,
        searchText = searchText,
        onSearchChange = onServiceSearchChange,
        label = "Services",
        searchPlaceholder = "Search services (e.g. MyService)"
    )
}
