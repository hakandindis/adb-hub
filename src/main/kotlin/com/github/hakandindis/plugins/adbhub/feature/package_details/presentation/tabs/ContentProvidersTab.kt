package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

@Composable
fun ContentProvidersTab(
    contentProviders: List<PackageDetails.ProviderInfo>,
    searchText: String,
    onContentProviderSearchChange: (String) -> Unit
) {
    val items = contentProviders.map { ComponentDisplay(name = it.name, exported = it.exported) }
    ComponentListTab(
        items = items,
        searchText = searchText,
        onSearchChange = onContentProviderSearchChange,
        label = "Content Providers",
        searchPlaceholder = "Search content providers (e.g. FileProvider)"
    )
}
