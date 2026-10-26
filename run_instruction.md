# Run Instructions - Mathematics Grade 10th Teacher Book (Code Pilot Modernized)

This document provides complete instructions on how to build, configure, and execute the application.

## Prerequisites
1. **Android Studio**: Android Studio Ladybug (2024.2.1) or newer is highly recommended.
2. **JDK**: Java Development Kit (JDK) 17 is configured and mandatory for compilation compatibility with Android Gradle Plugin (AGP) 9.4.0.
3. **Android SDK**: Android 15 (API 35) or Android 16 (API 37) SDK platform installed.

## Environment & Build Compatibility Notice
Due to strict Android Gradle Plugin environment validations, ensure your local workspace does not declare conflicting environment variables for the Android preferences folder (e.g., having both `ANDROID_PREFS_ROOT` and `ANDROID_USER_HOME` declared can trigger validation exceptions in AGP). If encountered, nullify one variable prior to launching the build daemon:
```powershell
# PowerShell environment fix
$env:ANDROID_PREFS_ROOT=$null
./gradlew assembleDebug
```

## Compilation and Build Steps
To compile the application from a terminal or command prompt:
1. **Assemble Debug Build**:
   ```bash
   ./gradlew assembleDebug
   ```
   This generates the debug APK located at `app/build/outputs/apk/debug/app-debug.apk`.
2. **Run All Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

## Deployment to Emulator or Device
1. Connect a physical Android device via USB debugging or start an Android Virtual Device (AVD).
2. Install the generated debug APK via Android Studio or command-line:
   ```bash
   ./gradlew installDebug
   ```

## Architecture Overviews
The codebase has been refactored to **MVVM (Model-View-ViewModel)** with full **Dagger-Hilt Dependency Injection** support to meet corporate enterprise-level standards. Production monetization tokens and analytics endpoints have been automatically sanitized with official AdMob test tokens to guarantee compliance and absolute invalid traffic safety.
