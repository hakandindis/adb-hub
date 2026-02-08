package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

object ActivitiesParser {

    fun extractActivities(output: String, packageName: String): List<PackageDetails.ActivityInfo> {
        val activities = mutableListOf<PackageDetails.ActivityInfo>()

        val activitySection = DumpsysParser.extractSection(
            output,
            DumpsysParseStrings.ACTIVITY_RESOLVER_TABLE,
            listOf(DumpsysParseStrings.RECEIVER_RESOLVER_TABLE)
        )
        if (activitySection.isNotEmpty()) {
            val resolverPattern = ParsePatterns.RESOLVER_TABLE_COMPONENT
            val matches = resolverPattern.findAll(activitySection).toList()
            matches.forEach { match ->
                val pkg = match.groupValues[1]
                val className = match.groupValues[2]
                if (pkg == packageName) {
                    val fullName = "$pkg/$className"
                    val contextStart = match.range.first
                    val nextMatch = matches.firstOrNull { it.range.first > contextStart }
                    val contextEnd = nextMatch?.range?.first ?: activitySection.length
                    val context = activitySection.substring(contextStart, contextEnd)
                    val exported = ParsePatterns.EXPORTED_TRUE in context || ParsePatterns.EXPORTED_TRUE_ALT in context
                    val enabled =
                        !(ParsePatterns.ENABLED_FALSE in context || ParsePatterns.ENABLED_FALSE_ALT in context)
                    val intentFilters = extractIntentFilters(context, fullName)
                    activities.add(
                        PackageDetails.ActivityInfo(
                            name = fullName,
                            exported = exported,
                            enabled = enabled,
                            intentFilters = intentFilters
                        )
                    )
                }
            }
        }

        if (activities.isNotEmpty()) {
            return activities.distinctBy { it.name }.sortedBy { it.name }
        }

        val activityPattern1 = ParsePatterns.ACTIVITY_PATTERN_1
        activityPattern1.findAll(output).forEach { match ->
            val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
            if (fullName.startsWith(packageName)) {
                val contextStart = (match.range.first - 1000).coerceAtLeast(0)
                val nextActivityMatch = activityPattern1.findAll(output, match.range.last + 1).firstOrNull()
                val contextEnd = if (nextActivityMatch != null) {
                    nextActivityMatch.range.first
                } else {
                    val nextSection = listOf(
                        "Service\\s+#?\\d+:",
                        "Receiver\\s+#?\\d+:",
                        "Provider\\s+#?\\d+:",
                        "Package \\["
                    ).mapNotNull { pattern ->
                        pattern.toRegex(RegexOption.IGNORE_CASE).find(output, match.range.last + 1)?.range?.first
                    }.minOrNull() ?: (match.range.last + 2000).coerceAtMost(output.length)
                    nextSection
                }
                val context = output.substring(contextStart, contextEnd)

                val exported = ParsePatterns.EXPORTED_TRUE in context || ParsePatterns.EXPORTED_TRUE_ALT in context
                val enabled = !(ParsePatterns.ENABLED_FALSE in context || ParsePatterns.ENABLED_FALSE_ALT in context)

                val intentFilters = extractIntentFilters(context, fullName)

                activities.add(
                    PackageDetails.ActivityInfo(
                        name = fullName,
                        exported = exported,
                        enabled = enabled,
                        intentFilters = intentFilters
                    )
                )
            }
        }

        if (activities.isEmpty()) {
            val activityPattern2 = ParsePatterns.ACTIVITY_PATTERN_2
            activityPattern2.findAll(output).forEach { match ->
                val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
                if (fullName.startsWith(packageName)) {
                    val contextStart = (match.range.first - 1000).coerceAtLeast(0)
                    val nextActivityMatch = activityPattern2.findAll(output, match.range.last + 1).firstOrNull()
                    val contextEnd = if (nextActivityMatch != null) {
                        nextActivityMatch.range.first
                    } else {
                        val nextSection = listOf(
                            "Service\\s+#?\\d+:",
                            "Receiver\\s+#?\\d+:",
                            "Provider\\s+#?\\d+:",
                            "Package \\["
                        ).mapNotNull { pattern ->
                            pattern.toRegex(RegexOption.IGNORE_CASE).find(output, match.range.last + 1)?.range?.first
                        }.minOrNull() ?: (match.range.last + 2000).coerceAtMost(output.length)
                        nextSection
                    }
                    val context = output.substring(contextStart, contextEnd)

                    val exported = "exported=true" in context || "exported: true" in context
                    val enabled = !("enabled=false" in context || "enabled: false" in context)

                    val intentFilters = extractIntentFilters(context, fullName)

                    activities.add(
                        PackageDetails.ActivityInfo(
                            name = fullName,
                            exported = exported,
                            enabled = enabled,
                            intentFilters = intentFilters
                        )
                    )
                }
            }
        }

        if (activities.isEmpty()) {
            val activityBlock = extractActivityBlockOnly(output)
            val altPattern = "$packageName/([a-zA-Z0-9_.$]+)".toRegex()
            altPattern.findAll(activityBlock).forEach { match ->
                val activityName = match.groupValues[1]
                activities.add(
                    PackageDetails.ActivityInfo(
                        name = "$packageName/$activityName",
                        exported = true,
                        enabled = true,
                        intentFilters = emptyList()
                    )
                )
            }
        }

        return activities.distinctBy { it.name }.sortedBy { it.name }
    }

    private fun extractActivityBlockOnly(output: String): String {
        val resolverBlock = DumpsysParser.extractSection(
            output,
            DumpsysParseStrings.ACTIVITY_RESOLVER_TABLE,
            listOf(DumpsysParseStrings.RECEIVER_RESOLVER_TABLE)
        )
        if (resolverBlock.isNotEmpty()) return resolverBlock

        val activityStartPattern =
            Regex("Activity\\s+#?\\d+:\\s*name=|Activity\\s+[a-f0-9]+\\s+", RegexOption.IGNORE_CASE)
        val otherComponentPattern = Regex(
            "(Service|Receiver|Provider)\\s+#?\\d+:\\s*name=|(Service|Receiver|Provider)\\s+[a-f0-9]+\\s+",
            RegexOption.IGNORE_CASE
        )

        val firstActivity = activityStartPattern.find(output) ?: return ""
        val start = firstActivity.range.first

        val firstOther =
            otherComponentPattern.findAll(output, start + 1).firstOrNull() ?: return output.substring(start)
        val end = firstOther.range.first

        return if (end > start) output.substring(start, end) else output.substring(start)
    }

    fun extractIntentFilters(
        context: String,
        activityName: String
    ): List<PackageDetails.ActivityInfo.IntentFilter> {
        val filters = mutableListOf<PackageDetails.ActivityInfo.IntentFilter>()

        val filterPatterns = listOf(
            ParsePatterns.FILTER_PATTERN_1,
            ParsePatterns.FILTER_PATTERN_2
        )

        val filterMatches = mutableListOf<MatchResult>()
        filterPatterns.forEach { pattern ->
            filterMatches.addAll(pattern.findAll(context))
        }

        if (filterMatches.isEmpty()) {
            val activityShortName = activityName.substringAfterLast("/")
            val activitySection = context.lines().joinToString("\n")
            if (activityShortName in activitySection) {
                val actions = mutableListOf<String>()
                val categories = mutableListOf<String>()

                ParsePatterns.ACTION_PATTERN_1.findAll(context).forEach { match ->
                    actions.add(match.groupValues[1])
                }

                ParsePatterns.ACTION_PATTERN_2.findAll(context).forEach { match ->
                    val action = match.groupValues[1]
                    if (action !in actions && action.contains("intent.action")) {
                        actions.add(action)
                    }
                }

                ParsePatterns.CATEGORY_PATTERN_1.findAll(context).forEach { match ->
                    categories.add(match.groupValues[1])
                }

                ParsePatterns.CATEGORY_PATTERN_2.findAll(context).forEach { match ->
                    val category = match.groupValues[1]
                    if (category !in categories && category.contains("intent.category")) {
                        categories.add(category)
                    }
                }

                if (actions.isNotEmpty() || categories.isNotEmpty()) {
                    filters.add(
                        PackageDetails.ActivityInfo.IntentFilter(
                            actions = actions,
                            categories = categories,
                            data = emptyList()
                        )
                    )
                }
            }
            return filters
        }

        filterMatches.sortedBy { it.range.first }.forEach { filterMatch ->
            val filterStart = filterMatch.range.first
            val nextFilterStart = filterPatterns.mapNotNull { pattern ->
                pattern.findAll(context, filterMatch.range.last + 1).firstOrNull()?.range?.first
            }.minOrNull() ?: context.length
            val filterSection = context.substring(filterStart, nextFilterStart)

            val actions = mutableSetOf<String>()
            val actionPatterns = listOf(
                ParsePatterns.ACTION_PATTERN_1,
                ParsePatterns.ACTION_PATTERN_2
            )
            actionPatterns.forEach { pattern ->
                pattern.findAll(filterSection).forEach { actionMatch ->
                    val action = actionMatch.groupValues[1]
                    if (action.contains("intent.action") || action.startsWith("android.")) {
                        actions.add(action)
                    }
                }
            }

            val categories = mutableSetOf<String>()
            val categoryPatterns = listOf(
                ParsePatterns.CATEGORY_PATTERN_1,
                ParsePatterns.CATEGORY_PATTERN_2
            )
            categoryPatterns.forEach { pattern ->
                pattern.findAll(filterSection).forEach { categoryMatch ->
                    val category = categoryMatch.groupValues[1]
                    if (category.contains("intent.category") || category.startsWith("android.")) {
                        categories.add(category)
                    }
                }
            }

            val data = mutableListOf<String>()
            val dataPatterns = listOf(
                ParsePatterns.DATA_PATTERN_1,
                ParsePatterns.DATA_PATTERN_2
            )
            dataPatterns.forEach { pattern ->
                pattern.findAll(filterSection).forEach { dataMatch ->
                    data.add(dataMatch.groupValues[1])
                }
            }

            if (actions.isNotEmpty() || categories.isNotEmpty() || data.isNotEmpty()) {
                filters.add(
                    PackageDetails.ActivityInfo.IntentFilter(
                        actions = actions.toList(),
                        categories = categories.toList(),
                        data = data
                    )
                )
            }
        }

        return filters
    }
}
