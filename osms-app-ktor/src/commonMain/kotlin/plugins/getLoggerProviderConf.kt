package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.logging.common.OsmsLoggerProvider

expect fun Application.getLoggerProviderConf(): OsmsLoggerProvider
