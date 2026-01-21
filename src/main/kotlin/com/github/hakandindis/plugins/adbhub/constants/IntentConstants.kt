package com.github.hakandindis.plugins.adbhub.constants

/**
 * Android Intent action and category constants
 */
object IntentConstants {
    /**
     * Intent Actions
     */
    object Actions {
        const val MAIN = "android.intent.action.MAIN"
        const val VIEW = "android.intent.action.VIEW"
    }

    /**
     * Intent Categories
     */
    object Categories {
        const val LAUNCHER = "android.intent.category.LAUNCHER"
    }

    /**
     * Checks if an action is MAIN
     */
    fun isMainAction(action: String): Boolean {
        return action.equals(Actions.MAIN, ignoreCase = true)
    }

    /**
     * Checks if a category is LAUNCHER
     */
    fun isLauncherCategory(category: String): Boolean {
        return category.equals(Categories.LAUNCHER, ignoreCase = true)
    }

    /**
     * Checks if an action is VIEW
     */
    fun isViewAction(action: String): Boolean {
        return action.contains("VIEW", ignoreCase = true)
    }
}
