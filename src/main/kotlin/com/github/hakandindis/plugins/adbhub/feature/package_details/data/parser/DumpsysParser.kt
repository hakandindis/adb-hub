package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns

object DumpsysParser {

    fun extractSection(output: String, startMarker: String, endMarkers: List<String>): String {
        val startIdx = output.indexOf(startMarker).takeIf { it >= 0 } ?: return ""
        val searchFrom = startIdx + startMarker.length
        val endIdx = endMarkers
            .mapNotNull { output.indexOf(it, searchFrom).takeIf { it > 0 } }
            .minOrNull()
            ?: output.length
        return output.substring(searchFrom, endIdx)
    }

    fun extractVersionName(output: String): String? {
        return ParsePatterns.VERSION_NAME.find(output)?.groupValues?.get(1)
    }

    fun extractVersionCode(output: String): String? {
        return ParsePatterns.VERSION_CODE.find(output)?.groupValues?.get(1)
    }

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

    fun extractDataDirectory(output: String): String? {
        return ParsePatterns.DATA_DIR.find(output)?.groupValues?.get(1)?.trim()
    }
}
