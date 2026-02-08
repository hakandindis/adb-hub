package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.tabs

import androidx.compose.runtime.Composable
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.ComponentDisplay

@Composable
fun ReceiversTab(
    receivers: List<ComponentDisplay>,
    searchText: String,
    onReceiverSearchChange: (String) -> Unit
) {
    ComponentListTab(
        items = receivers,
        searchText = searchText,
        onSearchChange = onReceiverSearchChange,
        label = "Receivers",
        searchPlaceholder = "Search receivers (e.g. BootReceiver)"
    )
}
