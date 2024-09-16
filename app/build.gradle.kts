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
        dataBinding = true
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
    implementation(project(":core:database"))
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
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.room.runtime)
    implementation(libs.room.core)
    implementation(libs.room.paging)

    kapt(libs.room.compiler)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.lifecycle.livedata.ktx.v261)
    implementation(libs.kotlinx.coroutines)
}