rootProject.name = "office-space-managment-system"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("io.kotest.multiplatform") version kotestVersion apply false
    }
}


include("osmp-api-v1")