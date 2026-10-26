# Run Instructions - Mathematics Grade 10th Teacher Book (Code Pilot Modernized)

[![Build System](https://img.shields.io/badge/Build%20System-Gradle%209.8.0-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org)
[![JDK Requirement](https://img.shields.io/badge/JDK-17-ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-API%2037%20%2F%20Android%2016-3DDC84.svg?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Test Runner](https://img.shields.io/badge/Test%20Runner-JUnit4%20%2B%20Mockito-C2185B.svg?style=for-the-badge)](https://site.mockito.org)

This document provides comprehensive technical instructions to configure, compile, test, and deploy the **Mathematics Grade 10th Teacher Book** application.

---

## 📋 Prerequisites & Environment Matrix

| Requirement | Recommended Version | Minimum Version | Notes |
| :--- | :--- | :--- | :--- |
| **IDE** | Android Studio Ladybug (2024.2.1+) | Android Studio Hedgehog | Standard IDE required |
| **JDK** | OpenJDK / JetBrains Runtime 17 | JDK 17 | Mandatory for AGP 9.4.1 compatibility |
| **Android SDK** | API Level 37 (Android 16) | API Level 35 (Android 15) | Installed via SDK Manager |
| **Build Tools** | `35.0.0` or newer | `34.0.0` | Standard Android SDK build tools |
| **Gradle / AGP** | Gradle 9.8.0 / AGP 9.4.1 | Gradle 8.11 / AGP 9.0 | Wrapper configured in project |

---

## ⚡ Environment & Build Compatibility Fixes

Due to strict Android Gradle Plugin environment variable validations on Windows workstations, ensure your local command session does not contain conflicting preference paths (e.g. having both `ANDROID_PREFS_ROOT` and `ANDROID_USER_HOME` set simultaneously can trigger validation exceptions):

### PowerShell / Windows Command Line Fix
```powershell
# Nullify conflicting variable before running Gradle
$env:ANDROID_PREFS_ROOT=$null
./gradlew assembleDebug
```

---

## ⚙️ Compilation & Testing Commands

Execute commands from the project root directory:

### 1. Assemble Debug APK
```bash
./gradlew assembleDebug
```
- **Output File**: `app/build/outputs/apk/debug/app-debug.apk`

### 2. Assemble Release APK / AAB
```bash
./gradlew assembleRelease
```
- **Output File**: `app/build/outputs/apk/release/app-release-unsigned.apk`

### 3. Run Automated Unit Test Suite
```bash
./gradlew testDebugUnitTest
```
- Runs unit tests for [BookRepositoryTest.java](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/test/java/com/beckytech/mathematicsgrade10thteacherbook/BookRepositoryTest.java) and [MainViewModelTest.java](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/test/java/com/beckytech/mathematicsgrade10thteacherbook/MainViewModelTest.java).
- **Test Results Report**: `app/build/reports/tests/testDebugUnitTest/index.html`

---

## 📲 Deployment to Device or Emulator

1. Connect an Android device with **USB Debugging** enabled, or launch an Android Virtual Device (AVD).
2. Verify connected devices:
   ```bash
   adb devices
   ```
3. Install and launch the debug build directly:
   ```bash
   ./gradlew installDebug
   ```

---

## 🛡️ Monetization & Sanitized Test Mode

To guarantee complete compliance with Google Play Store Invalid Traffic policies during public evaluation, all production AdMob App IDs and Unit IDs in [strings.xml](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/main/res/values/strings.xml) have been sanitized with official **Google AdMob Test Tokens**:

- **App ID**: `ca-app-pub-3940256099942544~3347511713`
- **Banner Unit ID**: `ca-app-pub-3940256099942544/6300978111`
- **Interstitial Unit ID**: `ca-app-pub-3940256099942544/1033173712`
- **App Open Ad Unit ID**: `ca-app-pub-3940256099942544/9257395921`
- **Rewarded Interstitial Unit ID**: `ca-app-pub-3940256099942544/5354046379`

To switch to production ad units, replace the strings in `res/values/strings.xml` with live production tokens.

---

## 🏛️ Architectural Overview

The codebase uses **MVVM (Model-View-ViewModel)** with **Dagger-Hilt Dependency Injection** and **ViewBinding**:
- `@HiltAndroidApp`: Initialized in [MyApplication.java](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/main/java/com/beckytech/mathematicsgrade10thteacherbook/MyApplication.java).
- `@Singleton`: Injected repositories and managers ([BookRepository.java](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/main/java/com/beckytech/mathematicsgrade10thteacherbook/BookRepository.java), [AdsManager.java](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/main/java/com/beckytech/mathematicsgrade10thteacherbook/AdsManager.java)).
- `@HiltViewModel`: Lifecycle-aware state presentation in [MainViewModel.java](file:///D:/AndroidStudioProjects/Mathematics-Grade-10th-Teacher-Book/app/src/main/java/com/beckytech/mathematicsgrade10thteacherbook/MainViewModel.java).
- `@AndroidEntryPoint`: Entry point injection across all activities.