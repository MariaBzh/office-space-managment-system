package ru.otus.osms.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import ru.otus.osms.logging.common.IOsmsLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun loggerKermit(loggerUid: String): IOsmsLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return OsmsLoggerWrapperKermit(
        logger = logger,
        loggerUid = loggerUid,
    )
}

@Suppress("unused")
fun loggerKermit(cls: KClass<*>): IOsmsLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return OsmsLoggerWrapperKermit(
        logger = logger,
        loggerUid = cls.qualifiedName?: "",
    )
}