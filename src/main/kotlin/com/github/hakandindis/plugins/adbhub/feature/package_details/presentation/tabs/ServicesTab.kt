package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

@Composable
fun ServicesTab(
    services: List<PackageDetails.ServiceInfo>,
    searchText: String,
    onServiceSearchChange: (String) -> Unit
) {
    val items = services.map { ComponentDisplay(name = it.name, exported = it.exported) }
    ComponentListTab(
        items = items,
        searchText = searchText,
        onSearchChange = onServiceSearchChange,
        label = "Services",
        searchPlaceholder = "Search services (e.g. MyService)"
    )
}
