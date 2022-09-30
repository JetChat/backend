import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    application
}

group = "fr.ayfri"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.core)
    implementation(libs.ktor.netty)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.defaultHeaders)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.websockets)
    
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.komapper.starter.jdbc)
    implementation(libs.komapper.mysql.jdbc)
    ksp(libs.komapper.processor)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}
