// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kapt) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
}