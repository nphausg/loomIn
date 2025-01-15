plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nphausg.foundation.test"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {  }
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
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    packaging {
        resources {
            excludes.add("/META-INF/*")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Test
    api(libs.bundles.unit.test)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.compose.runtime)
    implementation(platform(libs.androidx.compose.bom))
    // UI Test
//    androidTestImplementation(libs.bundles.androidx.ui.test)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    // Debug
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
}