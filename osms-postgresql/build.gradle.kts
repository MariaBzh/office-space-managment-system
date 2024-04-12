val postgresDriverVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jooq.jooq-codegen-gradle") version "3.19.7"
}

dependencies {
    jooqCodegen("org.postgresql:postgresql:$postgresDriverVersion")
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
                packageName = "jooq.codegen.models"
                directory = "build/generated-src/jooq/main"
            }
        }
    }
}
