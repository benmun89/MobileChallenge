plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.mobilechallenge.core.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "LANGUAGE", "\"en\"")
        buildConfigField("String", "API_KEY", "\"\"")
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org\"")
        buildConfigField("String", "TMDB_IMAGE_ORIGINAL_URL","\"https://image.tmdb.org/t/p/original\""
        )
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
    buildFeatures {
        buildConfig = true
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
    implementation(project(":core:model"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.espresso.core)
    implementation(libs.dagger)
    implementation(libs.hilt)
    implementation(libs.retrofit.rx.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    kapt(libs.dagger.compiler)
    kapt(libs.hilt.compiler)

    api(libs.bundles.retrofit)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation (libs.junit)
    testImplementation (libs.mockito.core.v5120)
    testImplementation (libs.mockito.inline)
    testImplementation (libs.retrofit.mock)
}