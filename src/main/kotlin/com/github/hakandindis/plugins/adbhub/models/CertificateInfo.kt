package com.github.hakandindis.plugins.adbhub.models

/**
 * Certificate information for an Android package
 */
data class CertificateInfo(
    val algorithm: String? = null,
    val validityFrom: String? = null,
    val validityTo: String? = null,
    val isValid: Boolean = false,
    val md5Fingerprint: String? = null,
    val sha1Fingerprint: String? = null,
    val sha256Fingerprint: String? = null,
    val subjectDN: String? = null,
    val serialNumber: String? = null
)
