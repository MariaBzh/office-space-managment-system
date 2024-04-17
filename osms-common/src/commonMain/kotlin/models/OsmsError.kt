package ru.otus.osms.common.models

import ru.otus.osms.logging.common.LogLevel

data class OsmsError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)