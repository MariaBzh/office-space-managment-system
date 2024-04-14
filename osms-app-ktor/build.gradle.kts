val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val testContainersVersion: String by project
val kmpUUIDVersion: String by project

fun ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"
fun ktorServer(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"
fun ktorClient(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

plugins {
    id("application")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("io.ktor.plugin")
}
dependencies {
    testImplementation("org.testng:testng:7.1.0")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.cio.EngineMain"
}

kotlin {
    jvm {
        withJava()
    }
    linuxX64 {}
    macosX64 {}


    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "ru.otus.osms.ktor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(ktorServer("core"))
                implementation(ktorServer("cio"))
                implementation(ktorServer("auto-head-response"))
                implementation(ktorServer("caching-headers"))
                implementation(ktorServer("cors"))
                implementation(ktorServer("config-yaml"))
                implementation(ktorServer("content-negotiation"))

                implementation(project(":osms-common"))
                implementation(project(":osms-app-common"))
                implementation(project(":osms-biz"))

                implementation(project(":osms-api-v1-kmp"))
                implementation(project(":osms-mappers-kmp-v1"))

                implementation(project(":osms-stubs"))

                implementation(project(":osms-api-log"))
                implementation(project(":osms-mappers-log"))
                implementation(project(":osms-logging-common"))
                implementation(project(":osms-logging-kermit"))

                implementation(project(":osms-in-memory"))
                implementation(project(":osms-repo-stubs"))

                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(project(":osms-repo-tests"))

                implementation(ktorServer("test-host"))
                implementation(ktorClient("content-negotiation"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                implementation(ktor("serialization-jackson"))
                implementation(ktorServer("call-logging"))
                implementation(ktorServer("default-headers"))

                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation(project(":osms-api-v1-jackson"))
                implementation(project(":osms-mappers-jackson-v1"))

                implementation(project(":osms-postgresql"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))

                implementation("org.testcontainers:postgresql:$testContainersVersion")
                implementation("com.benasher44:uuid:$kmpUUIDVersion")
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}