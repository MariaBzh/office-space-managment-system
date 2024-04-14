package ru.otus.osms.ktor.test.repo

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.osms.repo.pg.RepoBookingSQL
import ru.otus.osms.repo.pg.SqlProperties
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres")

object SqlTestCompanion {
    private const val USER = "osms-app"
    private const val PASS = "admin123"
    private const val SCHEMA = "osms"

    private const val INIT_SCRIPT = "init.sql"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withInitScript(INIT_SCRIPT)
            withStartupTimeout(Duration.ofSeconds(300L))
        }
    }

    fun start() {
        container.start()
    }

    fun stop() {
        container.stop()
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
       randomUuid: () -> String = { uuid4().toString() },
    ): RepoBookingSQL {
        return RepoBookingSQL(
            properties = SqlProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
            ),
            randomUuid = randomUuid
        )
    }
}