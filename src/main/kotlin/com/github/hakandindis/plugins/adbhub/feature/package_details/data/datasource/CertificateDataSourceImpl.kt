package com.github.hakandindis.plugins.adbhub.feature.package_details.data.datasource

import com.github.hakandindis.plugins.adbhub.constants.DumpsysCommands
import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.PmCommands
import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser.CertificateParser
import com.github.hakandindis.plugins.adbhub.models.CertificateInfo
import com.intellij.openapi.diagnostic.Logger

/**
 * Implementation of CertificateDataSource
 */
class CertificateDataSourceImpl(
    private val commandExecutor: AdbCommandExecutor
) : CertificateDataSource {

    private val logger = Logger.getInstance(CertificateDataSourceImpl::class.java)

    override suspend fun getCertificateInfo(packageName: String, deviceId: String): CertificateInfo? {
        return try {
            // Get APK path first
            val pathResult = commandExecutor.executeCommandForDevice(deviceId, PmCommands.getPackagePath(packageName))
            val apkPath = if (pathResult.isSuccess) {
                pathResult.output.lines()
                    .firstOrNull { it.startsWith(DumpsysParseStrings.PACKAGE_PREFIX) }
                    ?.substringAfter(DumpsysParseStrings.PACKAGE_PREFIX)
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
            } else {
                null
            }

            if (apkPath == null) {
                logger.warn("Could not get APK path for $packageName")
                return null
            }

            // Get certificate info using dumpsys or pm dump
            // Try dumpsys package first
            val dumpsysResult =
                commandExecutor.executeCommandForDevice(deviceId, DumpsysCommands.getPackageDumpsys(packageName))
            val dumpsysOutput = if (dumpsysResult.isSuccess) dumpsysResult.output else ""

            // Parse certificate info using CertificateParser
            val algorithm = CertificateParser.extractCertificateAlgorithm(dumpsysOutput)
            val validityFrom = CertificateParser.extractCertificateValidityFrom(dumpsysOutput)
            val validityTo = CertificateParser.extractCertificateValidityTo(dumpsysOutput)
            val isValid = CertificateParser.checkCertificateValidity(validityFrom, validityTo)
            val md5Fingerprint = CertificateParser.extractMD5Fingerprint(dumpsysOutput)
            val sha1Fingerprint = CertificateParser.extractSHA1Fingerprint(dumpsysOutput)
            val sha256Fingerprint = CertificateParser.extractSHA256Fingerprint(dumpsysOutput)
            val subjectDN = CertificateParser.extractSubjectDN(dumpsysOutput)
            val serialNumber = CertificateParser.extractSerialNumber(dumpsysOutput)

            CertificateInfo(
                algorithm = algorithm,
                validityFrom = validityFrom,
                validityTo = validityTo,
                isValid = isValid,
                md5Fingerprint = md5Fingerprint,
                sha1Fingerprint = sha1Fingerprint,
                sha256Fingerprint = sha256Fingerprint,
                subjectDN = subjectDN,
                serialNumber = serialNumber
            )
        } catch (e: Exception) {
            logger.error("Error getting certificate info for $packageName", e)
            null
        }
    }
}
