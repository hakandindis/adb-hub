package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns
import com.github.hakandindis.plugins.adbhub.constants.SystemPaths
import java.text.SimpleDateFormat
import java.util.*

object PackageDetailsParser {

    fun extractEnabledState(output: String): Boolean {
        val match = ParsePatterns.ENABLED_PATTERN.find(output)
        return match?.groupValues?.get(1)?.lowercase() == DumpsysParseStrings.TRUE
    }

    fun extractFirstInstallTime(output: String): String? {
        val match = ParsePatterns.FIRST_INSTALL_TIME.find(output)
        return match?.groupValues?.get(1)?.let { timestamp ->
            try {
                val time = timestamp.toLong()
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(time))
            } catch (e: Exception) {
                timestamp
            }
        }
    }

    fun extractLastUpdateTime(output: String): String? {
        val match = ParsePatterns.LAST_UPDATE_TIME.find(output)
        return match?.groupValues?.get(1)?.let { timestamp ->
            try {
                val time = timestamp.toLong()
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(time))
            } catch (e: Exception) {
                timestamp
            }
        }
    }

    fun isSystemApp(installLocation: String?): Boolean {
        return SystemPaths.isSystemAppPath(installLocation)
    }
}
