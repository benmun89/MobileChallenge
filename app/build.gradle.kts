plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.mobilechallenge"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.mobilechallenge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:util"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.paging)
    implementation(libs.glide)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.retrofit)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.hilt)
    implementation(libs.dagger)
    implementation(libs.livedata)
    implementation(libs.viewmodel)
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.fragment)
    implementation(libs.hilt)

    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}