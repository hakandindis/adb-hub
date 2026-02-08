package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

@Composable
fun ReceiversTab(
    receivers: List<PackageDetails.ReceiverInfo>,
    searchText: String,
    onReceiverSearchChange: (String) -> Unit
) {
    val items = receivers.map { ComponentDisplay(name = it.name, exported = it.exported) }
    ComponentListTab(
        items = items,
        searchText = searchText,
        onSearchChange = onReceiverSearchChange,
        label = "Receivers",
        searchPlaceholder = "Search receivers (e.g. BootReceiver)"
    )
}
