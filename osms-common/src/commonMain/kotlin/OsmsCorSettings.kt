package ru.otus.osms.common

import ru.otus.osms.logging.common.OsmsLoggerProvider

class OsmsCorSettings(
    val loggerProvider: OsmsLoggerProvider = OsmsLoggerProvider(),
) {
    companion object {
        val NONE = OsmsCorSettings()
    }
}