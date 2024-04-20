package ru.osms.app.common

import kotlinx.datetime.Clock
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.asOsmsError
import ru.otus.osms.common.models.OsmsState
import toLog
import kotlin.reflect.KClass

suspend inline fun <T> IOsmsAppSettings.controllerHelper(
    crossinline getRequest: suspend OsmsContext.() -> Unit,
    crossinline toResponse: suspend OsmsContext.() -> T,
    clazz: KClass<*>,
    logUid: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val context = OsmsContext(
        timeStart = Clock.System.now(),
    )

    return try {
        logger.doWithLogging(logUid) {
            context.getRequest()
            processor.exec(context)
            logger.info(
                message = "Request $logUid processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = context.toLog(logUid)
            )
            context.toResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging("$logUid-failure") {
            context.state = OsmsState.FAILING
            context.errors.add(e.asOsmsError())
            processor.exec(context)
            context.toResponse()
        }
    }
}