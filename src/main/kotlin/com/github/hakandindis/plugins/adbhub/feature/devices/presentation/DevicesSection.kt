package com.github.hakandindis.plugins.adbhub.feature.devices.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.hakandindis.plugins.adbhub.core.models.Device
import com.github.hakandindis.plugins.adbhub.feature.devices.presentation.ui.DeviceInfoItemUiModel
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.theme.AdbHubTheme
import com.github.hakandindis.plugins.adbhub.ui.theme.shapes.AdbHubShapes
import com.github.hakandindis.plugins.adbhub.ui.theme.typography.AdbHubTypography
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

@Composable
fun DevicesSection(
    devices: List<Device>,
    selectedDevice: Device?,
    deviceInfoItems: List<DeviceInfoItemUiModel>,
    onDeviceSelected: (Device) -> Unit,
    onRefreshDevices: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            "Connected Devices",
            style = AdbHubTypography.body,
            fontSize = 16.sp,
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(AdbHubShapes.SM)
                        .background(AdbHubTheme.colors.itemHover)
                        .border(1.dp, AdbHubTheme.colors.border, AdbHubShapes.SM)
                        .clickable { if (devices.isNotEmpty()) expanded = !expanded }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        selectedDevice?.displayName ?: "Select device",
                        style = AdbHubTypography.body
                    )
                    Icon(
                        AdbIcons.arrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (expanded && devices.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clip(AdbHubShapes.SM)
                            .background(AdbHubTheme.colors.surface)
                            .border(1.dp, AdbHubTheme.colors.border, AdbHubShapes.SM)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            items(devices, key = { it.id }) { device ->
                                val isSelected = device.id == selectedDevice?.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (isSelected) AdbHubTheme.colors.primary.copy(alpha = 0.22f)
                                            else AdbHubTheme.colors.surface
                                        )
                                        .clickable {
                                            onDeviceSelected(device)
                                            expanded = false
                                        }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        AdbIcons.smartphone,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 10.dp)
                                    ) {
                                        Text(
                                            device.displayName,
                                            style = AdbHubTypography.label
                                        )
                                        Text(
                                            device.id,
                                            style = AdbHubTypography.caption,
                                            color = AdbHubTheme.colors.textMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AdbHubTheme.colors.itemHover)
                    .border(1.dp, AdbHubTheme.colors.border, CircleShape)
                    .clickable(
                        onClick = {
                            onRefreshDevices?.invoke()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    AdbIcons.refresh,
                    contentDescription = "Refresh devices",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (selectedDevice != null && deviceInfoItems.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AdbHubShapes.SM)
                    .background(AdbHubTheme.colors.background)
                    .border(1.dp, AdbHubTheme.colors.border.copy(alpha = 0.5f), AdbHubShapes.SM)
                    .padding(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(deviceInfoItems, key = { it.label }) { item ->
                        DeviceDetailRow(label = item.label, value = item.value)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceDetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            label,
            style = AdbHubTypography.caption,
            color = AdbHubTheme.colors.textMuted
        )
        Text(
            value,
            style = AdbHubTypography.label,
            color = AdbHubTheme.colors.textMain
        )
    }
}
