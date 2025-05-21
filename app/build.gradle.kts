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
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // Media3 ExoPlayer dependencies
    // implementing Media3 so my app can play music. And all the other parts of it like session management and playback
    val media3Version = "androidx.media3:media3-ui:1.7.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version") // For DASH playback
    implementation("androidx.media3:media3-exoplayer-hls:$media3Version") // For HLS playback
    implementation("androidx.media3:media3-ui:$media3Version") // For UI components
    implementation("androidx.media3:media3-session:$media3Version") // For MediaSession integration

    val lifecycleVersion = "androidx.lifecycle:lifecycle-compiler:2.9.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion") // ViewModel utilities for Kotlin
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion") // Compose integration for ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion") // Compose integration with Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion") // LiveData with coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion") // SavedState for ViewModels

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Accompanist (Compose utilities)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")

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