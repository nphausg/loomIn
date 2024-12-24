plugins {
    id("java-library")
    id("maven-publish")
    kotlin("jvm")
}

fun getLocalGroup() = "com.nphausg"
fun getLocalVersion() = "0.0.1-alpha"
fun getLocalArtifactId() = "loom"

group = getLocalGroup()
version = getLocalVersion()

java {
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(kotlin("stdlib-jdk8"))
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