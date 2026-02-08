package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns

/**
 * Parser for extracting basic package information from dumpsys output
 */
object DumpsysParser {

    /**
     * Extracts the substring between startMarker and the first occurrence of any endMarker (after start).
     * Used to isolate Activity/Receiver/Service Resolver Table sections.
     */
    fun extractSection(output: String, startMarker: String, endMarkers: List<String>): String {
        val startIdx = output.indexOf(startMarker).takeIf { it >= 0 } ?: return ""
        val searchFrom = startIdx + startMarker.length
        val endIdx = endMarkers
            .mapNotNull { output.indexOf(it, searchFrom).takeIf { it > 0 } }
            .minOrNull()
            ?: output.length
        return output.substring(searchFrom, endIdx)
    }

    /**
     * Extracts version name from dumpsys output
     */
    fun extractVersionName(output: String): String? {
        return ParsePatterns.VERSION_NAME.find(output)?.groupValues?.get(1)
    }

    /**
     * Extracts version code from dumpsys output
     */
    fun extractVersionCode(output: String): String? {
        return ParsePatterns.VERSION_CODE.find(output)?.groupValues?.get(1)
    }

    /**
     * Extracts target SDK version from dumpsys output
     * Tries multiple patterns: targetSdk=XX (most common), targetSdkVersion=XX
     */
    fun extractTargetSdkVersion(output: String): String? {
        val patterns = listOf(
            ParsePatterns.TARGET_SDK,
            ParsePatterns.TARGET_SDK_VERSION
        )
        for (pattern in patterns) {
            pattern.find(output)?.groupValues?.get(1)?.let { return it }
        }
        return null
    }

    /**
     * Extracts min SDK version from dumpsys output
     * Tries multiple patterns: minSdk=XX (most common), minSdkVersion=XX
     */
    fun extractMinSdkVersion(output: String): String? {
        val patterns = listOf(
            ParsePatterns.MIN_SDK,
            ParsePatterns.MIN_SDK_VERSION
        )
        for (pattern in patterns) {
            pattern.find(output)?.groupValues?.get(1)?.let { return it }
        }
        return null
    }

    /**
     * Extracts data directory from dumpsys output
     */
    fun extractDataDirectory(output: String): String? {
        return ParsePatterns.DATA_DIR.find(output)?.groupValues?.get(1)?.trim()
    }
}
