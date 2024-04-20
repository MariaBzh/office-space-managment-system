package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.db.inmemory.BookingRepoInMemory
import ru.otus.osms.ktor.jvm.config.ConfigPaths
import ru.otus.osms.ktor.jvm.config.PostgresConfig
import ru.otus.osms.repo.pg.RepoBookingSQL
import ru.otus.osms.repo.pg.SqlProperties
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

actual fun Application.getDatabaseConf(type: BookingDbType): IBookingRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()

    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

private fun Application.initPostgres(): IBookingRepository {
    val config = PostgresConfig(environment.config)
    return RepoBookingSQL(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initInMemory(): IBookingRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return BookingRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}
