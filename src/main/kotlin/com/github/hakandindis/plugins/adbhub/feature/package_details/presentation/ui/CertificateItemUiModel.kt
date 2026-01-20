package com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui

/**
 * UI model for displaying certificate information
 */
data class CertificateItemUiModel(
    val label: String,
    val value: String,
    val type: CertificateItemType,
    val isValid: Boolean? = null
)

/**
 * Type of certificate item for different display styles
 */
enum class CertificateItemType {
    SIMPLE,      // Simple key-value display
    VALIDITY,    // Validity date range display
    FINGERPRINT  // Fingerprint display (monospace)
}
