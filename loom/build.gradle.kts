plugins {
    id("maven-publish")
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

fun getLocalGroup() = "com.nphausg"
fun getLocalArtifactId() = "loom"

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
}

publishing {
    publications {
        create<MavenPublication>("loom-publish") {
            run {
                groupId = getLocalGroup()
                artifactId = getLocalArtifactId()
                version = project.findProperty("version") as String? ?: "0.0.1-alpha"
                artifact("$buildDir/libs/${getLocalArtifactId()}-${version}.jar")
            }
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/nphausg/loomin")
            credentials {
                username = System.getenv("GPR_USERNAME") ?: project.findProperty("gpr.user") as String?
                password = System.getenv("GPR_TOKEN") ?: project.findProperty("gpr.token") as String?
            }
        }
    }
}