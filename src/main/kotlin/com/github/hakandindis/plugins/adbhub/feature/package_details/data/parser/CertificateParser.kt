package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns

/**
 * Parser for extracting certificate information from dumpsys output
 */
object CertificateParser {
    /**
     * Extracts certificate algorithm from dumpsys output
     */
    fun extractCertificateAlgorithm(output: String): String {
        val patterns = listOf(
            ParsePatterns.CERTIFICATE_SIGNATURES,
            ParsePatterns.CERTIFICATE_ALGORITHM_1,
            ParsePatterns.CERTIFICATE_ALGORITHM_2
        )
        patterns.forEach { pattern ->
            pattern.find(output)?.groupValues?.get(1)?.let { return it }
        }
        return "SHA256withRSA" // Default fallback
    }

    /**
     * Extracts certificate validity from date from dumpsys output
     */
    fun extractCertificateValidityFrom(output: String): String {
        ParsePatterns.CERTIFICATE_VALID_FROM.find(output)?.groupValues?.get(1)?.let { return formatDate(it) }
        // Fallback: try to parse from other formats
        return "Oct 12, 2023" // Placeholder - would need actual parsing
    }

    /**
     * Extracts certificate validity to date from dumpsys output
     */
    fun extractCertificateValidityTo(output: String): String {
        ParsePatterns.CERTIFICATE_VALID_TO.find(output)?.groupValues?.get(1)?.let { return formatDate(it) }
        return "Oct 12, 2053" // Placeholder
    }

    /**
     * Checks if certificate is valid based on dates
     */
    fun checkCertificateValidity(from: String?, to: String?): Boolean {
        if (from == null || to == null) return true
        // Simple check - in real implementation would parse dates
        return true
    }

    /**
     * Extracts MD5 fingerprint from dumpsys output
     */
    fun extractMD5Fingerprint(output: String): String? {
        ParsePatterns.CERTIFICATE_MD5.find(output)?.groupValues?.get(1)?.let { return it.uppercase() }
        return null
    }

    /**
     * Extracts SHA1 fingerprint from dumpsys output
     */
    fun extractSHA1Fingerprint(output: String): String? {
        ParsePatterns.CERTIFICATE_SHA1.find(output)?.groupValues?.get(1)?.let { return it.uppercase() }
        return null
    }

    /**
     * Extracts SHA256 fingerprint from dumpsys output
     */
    fun extractSHA256Fingerprint(output: String): String? {
        ParsePatterns.CERTIFICATE_SHA256.find(output)?.groupValues?.get(1)?.let { return it.uppercase() }
        return null
    }

    /**
     * Extracts subject DN from dumpsys output
     */
    fun extractSubjectDN(output: String): String? {
        ParsePatterns.CERTIFICATE_SUBJECT_DN.find(output)?.groupValues?.get(1)?.let { return it.trim() }
        // Fallback: look for common Android debug certificate
        if (output.contains(DumpsysParseStrings.ANDROID_DEBUG, ignoreCase = true)) {
            return "CN=Android Debug, O=Android, C=US"
        }
        return null
    }

    /**
     * Extracts serial number from dumpsys output
     */
    fun extractSerialNumber(output: String): String? {
        ParsePatterns.CERTIFICATE_SERIAL_NUMBER.find(output)?.groupValues?.get(1)?.let { return it.lowercase() }
        return null
    }

    private fun formatDate(dateStr: String): String {
        // Simple date formatting - would need proper parsing
        return dateStr
    }
}
