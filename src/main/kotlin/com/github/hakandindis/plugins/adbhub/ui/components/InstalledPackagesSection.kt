package com.github.hakandindis.plugins.adbhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.models.ApplicationPackage
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@Composable
fun InstalledPackagesSection(
    packages: List<ApplicationPackage>,
    selectedPackage: ApplicationPackage?,
    searchText: String,
    showSystem: Boolean,
    showUser: Boolean,
    showDebug: Boolean,
    onSearchChange: (String) -> Unit,
    onShowSystemChange: (Boolean) -> Unit,
    onShowUserChange: (Boolean) -> Unit,
    onShowDebugChange: (Boolean) -> Unit,
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
                placeholder = { Text("Search packages...") }
            )
            Box(
                modifier = Modifier
                    .clip(AdbHubShapes.XS)
                    .border(1.dp, AdbHubTheme.border, AdbHubShapes.XS)
                    .background(AdbHubTheme.surface)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("Ctrl+F", style = JewelTheme.defaultTextStyle)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip("System", showSystem, onShowSystemChange)
            FilterChip("User", showUser, onShowUserChange)
            FilterChip("Debug", showDebug, onShowDebugChange)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
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

@Composable
private fun FilterChip(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(AdbHubShapes.XS)
            .background(AdbHubTheme.background)
            .border(1.dp, AdbHubTheme.border, AdbHubShapes.XS)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(AdbHubShapes.XS)
                .background(if (checked) AdbHubTheme.primary else AdbHubTheme.background)
                .border(1.dp, AdbHubTheme.border, AdbHubShapes.XS),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Text("✓", style = JewelTheme.defaultTextStyle)
            }
        }
        Text(
            label,
            modifier = Modifier.padding(start = 6.dp),
            style = JewelTheme.defaultTextStyle
        )
    }
}
