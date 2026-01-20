package com.github.hakandindis.plugins.adbhub.feature.package_details.domain.mapper

import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.CertificateItemType
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.ui.CertificateItemUiModel
import com.github.hakandindis.plugins.adbhub.models.CertificateInfo

/**
 * Mapper for converting CertificateInfo to UI models
 */
object CertificateMapper {
    /**
     * Converts CertificateInfo to list of CertificateItemUiModel
     */
    fun toCertificateItems(certificateInfo: CertificateInfo?): List<CertificateItemUiModel> {
        if (certificateInfo == null) {
            return emptyList()
        }

        val items = mutableListOf<CertificateItemUiModel>()

        // Algorithm
        certificateInfo.algorithm?.let {
            items.add(
                CertificateItemUiModel(
                    label = "Algorithm",
                    value = it,
                    type = CertificateItemType.SIMPLE
                )
            )
        }

        // Validity
        items.add(
            CertificateItemUiModel(
                label = "Validity",
                value = buildValidityString(certificateInfo.validityFrom, certificateInfo.validityTo),
                type = CertificateItemType.VALIDITY,
                isValid = certificateInfo.isValid
            )
        )

        // Subject DN
        certificateInfo.subjectDN?.let {
            items.add(
                CertificateItemUiModel(
                    label = "Subject DN",
                    value = it,
                    type = CertificateItemType.SIMPLE
                )
            )
        }

        // Serial Number
        certificateInfo.serialNumber?.let {
            items.add(
                CertificateItemUiModel(
                    label = "Serial Number",
                    value = it,
                    type = CertificateItemType.SIMPLE
                )
            )
        }

        // Fingerprints
        certificateInfo.md5Fingerprint?.let {
            items.add(
                CertificateItemUiModel(
                    label = "MD5",
                    value = it,
                    type = CertificateItemType.FINGERPRINT
                )
            )
        }

        certificateInfo.sha1Fingerprint?.let {
            items.add(
                CertificateItemUiModel(
                    label = "SHA1",
                    value = it,
                    type = CertificateItemType.FINGERPRINT
                )
            )
        }

        certificateInfo.sha256Fingerprint?.let {
            items.add(
                CertificateItemUiModel(
                    label = "SHA256",
                    value = it,
                    type = CertificateItemType.FINGERPRINT
                )
            )
        }

        return items
    }

    /**
     * Builds validity string from from and to dates
     */
    private fun buildValidityString(from: String?, to: String?): String {
        return when {
            from != null && to != null -> "$from - $to"
            from != null -> "From: $from"
            to != null -> "Until: $to"
            else -> "N/A"
        }
    }
}
