package ru.otus.osms.repo.pg.test

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
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        test: String,
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoBookingSQL {
        return RepoBookingSQL(
            SqlProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
            ),
            randomUuid = randomUuid
        )
    }
}