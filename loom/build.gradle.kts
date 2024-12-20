plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("maven-publish")
}

fun getLocalGroup() = "com.nphausg"
fun getLocalVersion() = "0.0.1-alpha"
fun getLocalArtifactId() = "loom"

group = getLocalGroup()
version = getLocalVersion()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
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