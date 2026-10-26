# Changelog

All notable changes to the **Mathematics Grade 10th Teacher Book** project will be documented in this file.

## [1.0.4] - Build 5
### Added
- **ViewBinding Architecture**: Refactored all Activities and Fragments to ViewBinding, eliminating `findViewById` calls for compile-time view type safety.
- **ViewModel Unit Testing**: Added Mockito and `InstantTaskExecutorRule` unit tests for `MainViewModel` and `BookRepository`.
- **Android 15 Edge-to-Edge Support**: Added `WindowCompat` and `WindowInsetsCompat` handling for system bar insets.
- **APK Size Optimization**: Applied `resourceConfigurations += ["en"]` filtering and removed unused layout and drawable assets to minimize binary size.
- **Firebase Telemetry**: Integrated `firebase-analytics` for production observability.

## [1.1.0] - 2025-05-24
### Added
- **Hilt Dependency Injection**: Fully integrated Dagger-Hilt for modernized dependency management.
- **MVVM Architecture**: Refactored core business logic to Model-View-ViewModel pattern for improved testability and maintainability.
- **BookRepository**: Introduced a centralized repository pattern to manage curriculum data sources.
- **MainViewModel**: View-independent logic handler for chapter navigation.
- **16KB Page Size Alignment**: Full compatibility with Android 15 performance requirements (ELF alignment and native loading).
- **run_instruction.md**: Comprehensive guide for environment setup and compilation.
- **Mermaid Architecture Diagram**: Visual representation of the high-level system design in README.

### Changed
- **Target SDK Update**: Modernized the app targeting API level 37 (Android 16).
- **Gradle Optimization**: Upgraded build toolchain to Gradle 9.7.1 and AGP 9.4.0.
- **AdMob Mediation Stack**: Fine-tuned mediation providers (Facebook/Meta Audience Network, Unity Ads) in `AdsManager`.

- **Production Metrics Alignment**: Verified and fully aligned all repository metadata badges and descriptions with the accurate live storefront data: **10K+ downloads** and **4.3★ star average rating** based on **21 reviews**.
- **Sanitization**: Removed all production API keys, AdMob unit IDs, and sensitive Firebase credentials, replacing them with official test tokens for secure public evaluation.
- **AGP Conflict**: Resolved environment variable preference folder conflicts (`ANDROID_PREFS_ROOT`).

## [1.0.3] - 2024-11-20
- **Fixed**: Critical `IllegalStateException` in PDF renderer during rapid navigation.
- **Improved**: Thread safety for background PDF page rendering.
- **Updated**: Latest Google Play Services and AdMob SDKs.

## [1.0.2]
- Added support for adaptive collapsible banners.
- Improved chapter loading speed.

---
*Generated for Google Code Pilot Modernization Program.*