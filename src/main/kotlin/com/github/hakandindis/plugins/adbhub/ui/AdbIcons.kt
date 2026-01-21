package com.github.hakandindis.plugins.adbhub.ui

import org.jetbrains.jewel.ui.icons.AllIconsKeys

/**
 * Centralized icon keys for ADB Workflow UI.
 * Mapped to closest AllIconsKeys where Material-style names are noted.
 */
object AdbIcons {
    // Toolbar / header
    val plugConnect = AllIconsKeys.RunConfigurations.TestState.Run
    val settings = AllIconsKeys.Actions.Properties
    val wifi = AllIconsKeys.Nodes.Folder

    // Target device
    val smartphone = AllIconsKeys.Nodes.Module
    val arrowDropDown = AllIconsKeys.Actions.Expandall

    // Packages
    val search = AllIconsKeys.Actions.Find
    val android = AllIconsKeys.Nodes.Module
    val apps = AllIconsKeys.Nodes.ModuleGroup
    val arrowForward = AllIconsKeys.Actions.Forward

    // Quick actions
    val playArrow = AllIconsKeys.RunConfigurations.TestState.Run
    val stopCircle = AllIconsKeys.Run.Stop
    val cleaningServices = AllIconsKeys.General.Delete
    val delete = AllIconsKeys.General.Delete
    val link = AllIconsKeys.General.Information
    val bolt = AllIconsKeys.RunConfigurations.TestState.Run

    // Main tabs
    val info = AllIconsKeys.General.Information
    val terminal = AllIconsKeys.Toolwindows.ToolWindowRun
    val folder = AllIconsKeys.Nodes.Folder
    val folderOpen = AllIconsKeys.Nodes.Folder
    val queryStats = AllIconsKeys.General.Information

    // General
    val contentCopy = AllIconsKeys.Actions.Copy
    val settingsNode = AllIconsKeys.Nodes.Folder
}
