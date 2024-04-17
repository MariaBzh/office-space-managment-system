package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.ktor.OsmsAppSettings

fun Application.initAppSettings(): OsmsAppSettings {
    return OsmsAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = OsmsBookingProcessor(),
        corSettings = OsmsCorSettings(
            loggerProvider = getLoggerProviderConf(),
        ),
    )
}
