plugins {
    kotlin("jvm") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    application
}

group = "hu.kszi2"
version = "1.0-SNAPSHOT"

repositories {
    maven{
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("com.jessecorbett:diskord-bot:5.2.1-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")

    implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("org.jetbrains.exposed:exposed-crypt:0.43.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.43.0")
    implementation("org.jetbrains.exposed:exposed-json:0.43.0")
    implementation("org.jetbrains.exposed:exposed-money:0.43.0")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.43.0")
    implementation("org.xerial:sqlite-jdbc:3.30.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("hu.kszi2.boti.MainKt")
}

configurations {
    implementation {
        exclude("org.slf4j", "slf4j-simple")
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}