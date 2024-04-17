package ru.otus.osms.logging.common

import kotlinx.datetime.Clock
import kotlin.time.measureTimedValue

@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused")
interface IOsmsLogWrapper: AutoCloseable {
    val loggerUid: String

    fun log(
        message: String = "",
        level: LogLevel = LogLevel.TRACE,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objects: Map<String, Any>? = null,
    )

    fun error(
        message: String = "",
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) = log(message, LogLevel.ERROR, marker, e, data, objects)

    fun info(
        message: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) = log(message, LogLevel.INFO, marker, null, data, objects)

    fun debug(
        message: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) = log(message, LogLevel.DEBUG, marker, null, data, objects)

    /**
     * Функция обертка для выполнения прикладного кода с логированием перед выполнением и после
     */
    suspend fun <T> doWithLogging(
        uid: String = "",
        level: LogLevel = LogLevel.INFO,
        block: suspend () -> T,
    ): T = try {
        log("Started $loggerUid $uid", level)
        val (res, diffTime) = measureTimedValue { block() }

        log(
            message = "Finished $loggerUid $uid",
            level = level,
            objects = mapOf("metricHandleTime" to diffTime.toIsoString())
        )
        res
    } catch (e: Throwable) {
        log(
            message = "Failed $loggerUid $uid",
            level = LogLevel.ERROR,
            e = e
        )
        throw e
    }

    /**
     * Функция обертка для выполнения прикладного кода с логированием ошибки
     */
    suspend fun <T> doWithErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (e: Throwable) {
        log(
            message = "Failed $loggerUid $id",
            level = LogLevel.ERROR,
            e = e
        )
        if (throwRequired) throw e else null
    }

    override fun close() {}

    companion object {
        val DEFAULT = object: IOsmsLogWrapper {
            override val loggerUid: String = "NONE"

            override fun log(
                message: String,
                level: LogLevel,
                marker: String,
                e: Throwable?,
                data: Any?,
                objects: Map<String, Any>?,
            ) {
                val markerString = marker
                    .takeIf { it.isNotBlank() }
                    ?.let { " ($it)" }
                val args = listOfNotNull(
                    "${Clock.System.now()} [${level.name}]$markerString: $message",
                    e?.let { "${it.message ?: "Unknown reason"}:\n${it.stackTraceToString()}" },
                    data.toString(),
                    objects.toString(),
                )
                println(args.joinToString("\n"))
            }

        }
    }
}