package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay

@Composable
fun ContentProvidersTab(
    contentProviders: List<ComponentDisplay>,
    searchText: String,
    onContentProviderSearchChange: (String) -> Unit
) {
    ComponentListTab(
        items = contentProviders,
        searchText = searchText,
        onSearchChange = onContentProviderSearchChange,
        label = "Content Providers",
        searchPlaceholder = "Search content providers (e.g. FileProvider)"
    )
}
