package ru.otus.osms.ktor.jvm.config

import io.ktor.server.config.*

data class PostgresConfig(
    val url: String = "jdbc:postgresql://localhost:5432/osms",
    val user: String = "osms-app",
    val password: String = "admin123",
    val schema: String = "osms",
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "${ConfigPaths.repository}.psql"
    }
}