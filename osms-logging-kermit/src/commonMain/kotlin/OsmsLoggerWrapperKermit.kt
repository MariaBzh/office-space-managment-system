package ru.otus.osms.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import ru.otus.osms.logging.common.IOsmsLogWrapper
import ru.otus.osms.logging.common.LogLevel

class OsmsLoggerWrapperKermit (
    val logger: Logger,
    override val loggerUid: String
) : IOsmsLogWrapper {
        override fun log(
            message: String,
            level: LogLevel,
            marker: String,
            e: Throwable?,
            data: Any?,
            objects: Map<String,Any>?
        ) {
            logger.log(
                severity = level.toKermit(),
                tag = marker,
                throwable = e,
                message = formatMessage(message, data, objects),
            )
        }

        private fun LogLevel.toKermit() = when(this) {
            LogLevel.ERROR -> Severity.Error
            LogLevel.WARN -> Severity.Warn
            LogLevel.INFO -> Severity.Info
            LogLevel.DEBUG -> Severity.Debug
            LogLevel.TRACE -> Severity.Verbose
        }

        private inline fun formatMessage(
            message: String = "",
            data: Any? = null,
            objects: Map<String,Any>? = null
        ): String {
            var formatMessage = message
            data?.let {
                formatMessage += "\n" + data.toString()
            }
            objects?.forEach {
                formatMessage += "\n" + it.toString()
            }
            return formatMessage
        }
}