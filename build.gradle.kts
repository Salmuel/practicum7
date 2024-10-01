// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jetbrains.kotlin.kapt") version "1.8.20" apply false
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains:annotations:13.0")
        }
        exclude(group = "com.intellij", module = "annotations")
    }
}
