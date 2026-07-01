# AdMob
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }

# Facebook Mediation
-keep class com.facebook.ads.** { *; }
-keep class com.google.ads.mediation.facebook.** { *; }

# Unity Mediation
-keep class com.unity3d.ads.** { *; }
-keep class com.google.ads.mediation.unity.** { *; }
-keep class com.unity3d.services.** { *; }

# General R8/ProGuard rules
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontwarn com.google.android.gms.**
-dontwarn com.facebook.ads.**
-dontwarn com.unity3d.**

# Support for 16KB page sizes and native libs
-keep class androidx.startup.InitializationProvider { *; }
-keep class androidx.work.impl.WorkDatabase { *; }
-dontwarn androidx.work.impl.WorkDatabasePathHelper

# Handle missing classes reported by R8
-ignorewarnings
-keep class * extends java.util.List { *; }
-keep class com.beckytech.mathematicsgrade10thteacherbook.model.** { *; }