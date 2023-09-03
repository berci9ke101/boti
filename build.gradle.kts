plugins {
    kotlin("jvm") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    application
}

group = "hu.kszi2"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.jessecorbett:diskord-bot:5.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}

configurations {
    implementation {
        exclude("org.slf4j", "slf4j-simple")
    }
}