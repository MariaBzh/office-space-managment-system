rootProject.name = "office-space-management-system"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings
    val cwpGeneratorVersioin: String by settings
    val ktorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("com.crowdproj.generator") version cwpGeneratorVersioin apply false
        id("io.ktor.plugin") version ktorVersion apply false
    }
}


include("osms-api-v1-jackson")
include("osms-api-v1-kmp")
include("osms-common")
include("osms-mappers-jackson-v1")
include("osms-mappers-kmp-v1")
include("osms-stubs")
include("osms-biz")
include("osms-app-ktor")
include("osms-app-kafka")
include("osms-app-common")
include("osms-logging-common")
include("osms-logging-kermit")
include("osms-api-log")
include("osms-mappers-log")
