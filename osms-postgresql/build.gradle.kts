val postgresDriverVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jooq.jooq-codegen-gradle") version "3.19.7"
}

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val kmpUUIDVersion: String by project
    val testContainersVersion: String by project
    val jooqVersion: String by project

    implementation("org.jooq:jooq:$jooqVersion")

    jooqCodegen("org.postgresql:postgresql:$postgresDriverVersion")

    implementation(kotlin("stdlib"))

    implementation(project(":osms-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.benasher44:uuid:$kmpUUIDVersion")


    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation(project(":osms-repo-tests"))
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5432/osms"
            user = "osms-app"
            password = "admin123"
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "osms"
            }
            target {
                packageName = "${rootProject.group}.repo"
                directory = "build/generate-resources/main/src/main/kotlin"
            }
        }
    }
}
