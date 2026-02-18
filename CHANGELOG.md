# ADB Hub Changelog

## [Unreleased]

## [0.2.0] - 2026-02-18

### Changed

- **Custom Result Mechanism** – Replaced Kotlin `Result` with custom `AdbHubResult` and `AdbHubError` for better error
  handling
- **ADB Path Finding** – Improved cross-platform ADB path detection for macOS, Windows, and Linux

### Fixed

- **Deep Link Execution** – Removed redundant package name from deep link commands

## [0.1.0] - 2026-02-15

Initial release. Manage Android devices and applications directly from your IDE.

### Added

- **Device Management** – List USB/wireless devices, view device info, switch between devices
- **Package Browser** – Search and filter installed apps, view system packages
- **Package Details** – General info, App Links, Permissions, Activities, Receivers, Services, Content Providers
- **App Actions** – Launch, force stop, clear data, uninstall, clear cache, send deep links
- **ADB Console** – Real-time log of executed ADB commands
- **Theme Support** – Light and dark theme with IDE integration
- **Compose UI** – Modern Jetpack Compose (Jewel) interface
