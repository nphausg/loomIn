plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Required -- JUnit 4 framework
    testImplementation(libs.junit)
    // Optional -- Robolectric environment
    testImplementation("androidx.test:core:1.6.1")
    // Optional -- Mockito framework
    testImplementation("org.mockito:mockito-core:3.11.2")
    // Optional -- mockito-kotlin
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    // Optional -- Mockk framework
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}