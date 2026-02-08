package com.github.hakandindis.plugins.adbhub.feature.package_details.data.parser

import com.github.hakandindis.plugins.adbhub.constants.DumpsysParseStrings
import com.github.hakandindis.plugins.adbhub.constants.ParsePatterns
import com.github.hakandindis.plugins.adbhub.models.PackageDetails

/**
 * Parser for extracting activities and intent filters from dumpsys output
 */
object ActivitiesParser {
    /**
     * Extracts activities from dumpsys output.
     * Supports: (1) Activity Resolver Table format (hexId package/Class filter),
     * (2) Legacy "Activity #0: name=package/Class", (3) fallback packageName/Class in activity block.
     */
    fun extractActivities(output: String, packageName: String): List<PackageDetails.ActivityInfo> {
        val activities = mutableListOf<PackageDetails.ActivityInfo>()

        // Resolver table format: "d2af05a com.Slack/slack.features.home.HomeActivity filter c02cf68"
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

        // Pattern 1: "Activity #0: name=com.example/.MainActivity"
        val activityPattern1 = ParsePatterns.ACTIVITY_PATTERN_1
        activityPattern1.findAll(output).forEach { match ->
            val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
            if (fullName.startsWith(packageName)) {
                // Expand context window to capture more intent filter information
                val contextStart = (match.range.first - 1000).coerceAtLeast(0)
                // Find the end of this activity section (next Activity or major section)
                val nextActivityMatch = activityPattern1.findAll(output, match.range.last + 1).firstOrNull()
                val contextEnd = if (nextActivityMatch != null) {
                    nextActivityMatch.range.first
                } else {
                    // Look for next major section (Service, Receiver, etc.)
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

                // Extract intent filters
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

        // Pattern 2: "Activity [hex] com.example/.MainActivity"
        if (activities.isEmpty()) {
            val activityPattern2 = ParsePatterns.ACTIVITY_PATTERN_2
            activityPattern2.findAll(output).forEach { match ->
                val fullName = "${match.groupValues[1]}/${match.groupValues[2]}"
                if (fullName.startsWith(packageName)) {
                    // Expand context window to capture more intent filter information
                    val contextStart = (match.range.first - 1000).coerceAtLeast(0)
                    // Find the end of this activity section (next Activity or major section)
                    val nextActivityMatch = activityPattern2.findAll(output, match.range.last + 1).firstOrNull()
                    val contextEnd = if (nextActivityMatch != null) {
                        nextActivityMatch.range.first
                    } else {
                        // Look for next major section (Service, Receiver, etc.)
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

        // Pattern 3: Simple pattern for packageName/ActivityName — only within the Activity block
        // (dumpsys has Activity #0..., then Service #0..., Receiver #0..., Provider #0...;
        //  we must not match Service/Receiver/Provider class names as activities)
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

    /**
     * Returns the substring of dumpsys output that contains only Activity entries.
     * Prefers "Activity Resolver Table:" up to "Receiver Resolver Table:"; otherwise
     * from first "Activity #" or "Activity [hex]" up to first Service/Receiver/Provider line.
     */
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

    /**
     * Extracts intent filters from activity context
     */
    fun extractIntentFilters(
        context: String,
        activityName: String
    ): List<PackageDetails.ActivityInfo.IntentFilter> {
        val filters = mutableListOf<PackageDetails.ActivityInfo.IntentFilter>()

        // Look for intent filter sections - try multiple patterns
        val filterPatterns = listOf(
            ParsePatterns.FILTER_PATTERN_1,
            ParsePatterns.FILTER_PATTERN_2
        )

        val filterMatches = mutableListOf<MatchResult>()
        filterPatterns.forEach { pattern ->
            filterMatches.addAll(pattern.findAll(context))
        }

        if (filterMatches.isEmpty()) {
            // Fallback: try to extract from activity resolver table format
            // Look for lines with Action: and Category: near the activity name
            val activityShortName = activityName.substringAfterLast("/")
            val activitySection = context.lines().joinToString("\n")
            if (activityShortName in activitySection) {
                // Try to find actions and categories in the context
                val actions = mutableListOf<String>()
                val categories = mutableListOf<String>()

                // Pattern 1: Action: "android.intent.action.MAIN"
                ParsePatterns.ACTION_PATTERN_1.findAll(context).forEach { match ->
                    actions.add(match.groupValues[1])
                }

                // Pattern 2: Action: android.intent.action.MAIN (without quotes)
                ParsePatterns.ACTION_PATTERN_2.findAll(context).forEach { match ->
                    val action = match.groupValues[1]
                    if (action !in actions && action.contains("intent.action")) {
                        actions.add(action)
                    }
                }

                // Pattern 1: Category: "android.intent.category.LAUNCHER"
                ParsePatterns.CATEGORY_PATTERN_1.findAll(context).forEach { match ->
                    categories.add(match.groupValues[1])
                }

                // Pattern 2: Category: android.intent.category.LAUNCHER (without quotes)
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

        // Sort matches by position
        filterMatches.sortedBy { it.range.first }.forEach { filterMatch ->
            val filterStart = filterMatch.range.first
            val nextFilterStart = filterPatterns.mapNotNull { pattern ->
                pattern.findAll(context, filterMatch.range.last + 1).firstOrNull()?.range?.first
            }.minOrNull() ?: context.length
            val filterSection = context.substring(filterStart, nextFilterStart)

            // Extract actions - try multiple patterns
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

            // Extract categories - try multiple patterns
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

            // Extract data
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
