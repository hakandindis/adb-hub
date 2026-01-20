package com.github.hakandindis.plugins.adbhub.ui.components.details

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.CertificateItemType
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.CertificateItemUiModel
import com.github.hakandindis.plugins.adbhub.ui.AdbIcons
import com.github.hakandindis.plugins.adbhub.ui.components.common.CertificateFingerprintRow
import com.github.hakandindis.plugins.adbhub.ui.components.common.CertificateRow
import com.github.hakandindis.plugins.adbhub.ui.components.common.CertificateValidityRow
import com.github.hakandindis.plugins.adbhub.ui.components.common.ListSection
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun CertificateTab(
    certificateItems: List<CertificateItemUiModel>
) {
    if (certificateItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Certificate information not available",
                style = JewelTheme.defaultTextStyle
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ListSection(
            title = "SIGNING CERTIFICATE",
            icon = AdbIcons.contentCopy
        ) {
            Column {
                certificateItems.forEachIndexed { index, item ->
                    when (item.type) {
                        CertificateItemType.VALIDITY -> {
                            val parts = item.value.split(" - ")
                            if (parts.size == 2) {
                                CertificateValidityRow(
                                    from = parts[0],
                                    to = parts[1],
                                    isValid = item.isValid ?: true,
                                    isLast = index == certificateItems.size - 1
                                )
                            } else {
                                CertificateRow(
                                    label = item.label,
                                    value = item.value,
                                    isMonospace = false,
                                    isLast = index == certificateItems.size - 1
                                )
                            }
                        }

                        CertificateItemType.FINGERPRINT -> {
                            CertificateFingerprintRow(
                                label = item.label,
                                value = item.value,
                                isLast = index == certificateItems.size - 1
                            )
                        }

                        else -> {
                            CertificateRow(
                                label = item.label,
                                value = item.value,
                                isMonospace = false,
                                isLast = index == certificateItems.size - 1
                            )
                        }
                    }
                }
            }
        }
    }
}

