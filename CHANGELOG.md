# ADB Hub Changelog

## [Unreleased]

## [1.0.0-alpha] - 2025-02-13

Initial alpha release. Enables Android developers to manage connected devices and applications directly from the IDE.

### Added

- **Device Management**
    - List connected Android devices (USB and wireless)
    - View device info (model, state, API level)
    - Switch between multiple devices

- **Package List**
    - List installed applications on the selected device
    - Search and filter packages
    - View all packages including system apps

- **Package Details**
    - General info (version, package name, app name)
    - Inspect App Links definitions
    - Permissions list with search
    - Activities, Receivers, Services, Content Providers lists (search supported)

- **App Actions**
    - Launch app
    - Force stop app
    - Clear app data
    - Uninstall app
    - Clear app cache
    - Launch deep link (via implicit intent)

- **ADB Console**
    - View log of executed ADB commands

- **IDE Integration**
    - Tool Window access
    - Compatible with JetBrains AI (2025.2+) and Android Studio
    - Compose-based UI
