plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.nphausg.app.foundation.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    api(libs.androidx.ui)
    api(libs.androidx.core.ktx)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.activity.compose)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.material3)
    api(libs.androidx.compose.runtime)
    implementation(platform(libs.androidx.compose.bom))
    // Debug
    debugApi(libs.androidx.ui.tooling)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
