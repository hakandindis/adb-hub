package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@Composable
fun InstalledPackagesSection(
    packages: List<ApplicationPackage>,
    selectedPackage: ApplicationPackage?,
    searchText: String,
    onSearchChange: (String) -> Unit,
    onPackageSelected: (ApplicationPackage) -> Unit
) {
    val searchState = rememberTextFieldState(searchText)
    LaunchedEffect(searchText) { searchState.setTextAndPlaceCursorAtEnd(searchText) }
    LaunchedEffect(searchState.text.toString()) { onSearchChange(searchState.text.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(AdbIcons.search, contentDescription = null, modifier = Modifier.size(16.dp))
            TextField(
                state = searchState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                placeholder = { Text("Filter packages...") }
            )
        }

        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (packages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No packages found",
                        style = JewelTheme.defaultTextStyle
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(packages) { pkg ->
                        PackageListItem(
                            packageItem = pkg,
                            isSelected = pkg.packageName == selectedPackage?.packageName,
                            onClick = { onPackageSelected(pkg) }
                        )
                    }
                }
            }
        }
    }
}
