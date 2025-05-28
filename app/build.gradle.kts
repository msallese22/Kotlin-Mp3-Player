plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mycoolmusicplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mycoolmusicplayer"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Additional Compose Dependencies
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.media3.common.ktx)

    // Media3 ExoPlayer dependencies
    // implementing Media3 so my app can play music. And all the other parts of it like session management and playback
    val media3Version = "androidx.media3:media3-ui:1.7.1"
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash) // For DASH playback
    implementation(libs.androidx.media3.exoplayer.hls) // For HLS playback
    implementation(libs.androidx.media3.ui) // For UI components
    implementation(libs.androidx.media3.session) // For MediaSession integration

    val lifecycleVersion = "androidx.lifecycle:lifecycle-compiler:2.9.0"
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel utilities for Kotlin
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Compose integration for ViewModel
    implementation(libs.androidx.lifecycle.runtime.compose) // Compose integration with Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx) // LiveData with coroutines
    implementation(libs.androidx.lifecycle.viewmodel.savedstate) // SavedState for ViewModels

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Accompanist (Compose utilities)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation.animation)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)


    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}