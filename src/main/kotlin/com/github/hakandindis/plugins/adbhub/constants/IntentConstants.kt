package com.github.hakandindis.plugins.adbhub.constants

object IntentConstants {
    object Actions {
        const val MAIN = "android.intent.action.MAIN"
        const val VIEW = "android.intent.action.VIEW"
    }

    object Categories {
        const val LAUNCHER = "android.intent.category.LAUNCHER"
    }

    fun isMainAction(action: String): Boolean =
        action.equals(Actions.MAIN, ignoreCase = true)

    fun isLauncherCategory(category: String): Boolean =
        category.equals(Categories.LAUNCHER, ignoreCase = true)

    fun isViewAction(action: String): Boolean =
        action.contains("VIEW", ignoreCase = true)
}
