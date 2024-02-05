val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project

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
    macosArm64 {}

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
                implementation(project(":osms-biz"))

                implementation(project(":osms-api-v1-kmp"))
                implementation(project(":osms-mappers-kmp-v1"))

                implementation(project(":osms-stubs"))

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
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}