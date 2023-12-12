plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val jacksonVersion: String by project
    val junitJupiterVersion: String by project
    implementation(kotlin("stdlib"))
    implementation(project(":osms-api-v1-jackson"))
    implementation(project(":osms-common"))

    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }
}