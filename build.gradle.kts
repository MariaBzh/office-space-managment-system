import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") apply  false
    kotlin("multiplatform") apply false
}

val jvmTarget = "17"

group = "ru.otus.osms"
version = "0.0.1"

repositories {
    allprojects {
        repositories {
            google()
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = jvmTarget
    }
    tasks.withType<KotlinJvmCompile> {
        kotlinOptions.jvmTarget = jvmTarget
    }
}