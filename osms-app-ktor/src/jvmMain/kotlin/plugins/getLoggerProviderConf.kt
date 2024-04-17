package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.logging.common.OsmsLoggerProvider
import ru.otus.osms.logging.kermit.loggerKermit

actual fun Application.getLoggerProviderConf(): OsmsLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp", null -> OsmsLoggerProvider { loggerKermit(it) }

        else -> throw Exception("Logger $mode is not allowed. Admitted values are kmp (default)")
    }