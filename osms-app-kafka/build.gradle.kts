plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("ru.otus.osms.kafka.MainKt")
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation(project(":osms-common"))
    implementation(project(":osms-api-v1-jackson"))
    implementation(project(":osms-api-v1-kmp"))
    implementation(project(":osms-mappers-jackson-v1"))
    implementation(project(":osms-mappers-kmp-v1"))
    implementation(project(":osms-biz"))


    testImplementation(kotlin("test-junit"))
}
