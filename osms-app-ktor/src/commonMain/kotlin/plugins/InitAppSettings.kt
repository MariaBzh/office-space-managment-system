package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.repo.stubs.BookingRepoStub

fun Application.initAppSettings(): OsmsAppSettings {
    val corSettings = OsmsCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(BookingDbType.TEST),
        repoProd = getDatabaseConf(BookingDbType.PROD),
        repoStub = BookingRepoStub(),
    )

    return OsmsAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = OsmsBookingProcessor(corSettings),
    )
}
