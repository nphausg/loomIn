plugins {
    id("maven-publish")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

fun getLocalGroup() = "com.nphausg"
fun getLocalVersion() = "0.0.1-alpha"
fun getLocalArtifactId() = "loom"

group = getLocalGroup()
version = getLocalVersion()

android {
    namespace = "com.nphausg.loom"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug { }
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
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.androidx.test.rules)
}

publishing {
    publications {
        create<MavenPublication>("loom-publish") {
            run {
                groupId = getLocalGroup()
                artifactId = getLocalArtifactId()
                version = getLocalVersion()
                artifact("$buildDir/libs/${getLocalArtifactId()}-${getLocalVersion()}.jar")
            }
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/nphausg/loomin")
            credentials {
                username = System.getenv("GPR_USER")
                password = System.getenv("GPR_API_KEY")
            }
        }
    }
}