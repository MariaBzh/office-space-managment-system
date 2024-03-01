package ru.osms.app.common

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings

interface IOsmsAppSettings {
    val processor: OsmsBookingProcessor
    val corSettings: OsmsCorSettings
}
