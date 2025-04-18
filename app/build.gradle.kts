plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.navigation.safeargs)
}

// Load local.properties
val localProperties = java.util.Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.likeai.ecommerce"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.likeai.ecommerce"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add API keys as BuildConfig fields
        buildConfigField("String", "FIREBASE_API_KEY", "\"${localProperties.getProperty("FIREBASE_API_KEY", "")}\"")
        buildConfigField("String", "FIREBASE_APP_ID", "\"${localProperties.getProperty("FIREBASE_APP_ID", "")}\"")
        buildConfigField("String", "FIREBASE_PROJECT_ID", "\"${localProperties.getProperty("FIREBASE_PROJECT_ID", "")}\"")
        buildConfigField("String", "FIREBASE_STORAGE_BUCKET", "\"${localProperties.getProperty("FIREBASE_STORAGE_BUCKET", "")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    
    // Architecture Components
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    
    // Google Sign In
    implementation(libs.play.services.auth)

    // Image Loading
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Coroutines
    implementation(libs.bundles.coroutines)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.espresso)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    // Material Design 3
    implementation(libs.compose.material3)
    
    // Foundation
    implementation(libs.compose.foundation)
    
    // Integration with activities
    implementation(libs.activity.compose)
    
    // Integration with ViewModels
    implementation(libs.lifecycle.viewmodel.compose)
    
    // UI Tests
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    
    // Preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    
    // Optional - Integration with LiveData
    implementation(libs.compose.runtime.livedata)
}