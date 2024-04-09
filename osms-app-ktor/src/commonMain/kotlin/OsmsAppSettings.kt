package ru.otus.osms.ktor

import ru.osms.app.common.IOsmsAppSettings
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings

data class OsmsAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: OsmsCorSettings = OsmsCorSettings(),
    override val processor: OsmsBookingProcessor = OsmsBookingProcessor(corSettings),
) : IOsmsAppSettings
