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
    val ctx = OsmsContext(
        timeStart = Clock.System.now(),
    )

    return try {
        logger.doWithLogging(logUid) {
            ctx.getRequest()
            processor.exec(ctx)
            logger.info(
                message = "Request $logUid processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = ctx.toLog(logUid)
            )
            ctx.toResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging("$logUid-failure") {
            ctx.state = OsmsState.FAILING
            ctx.errors.add(e.asOsmsError())
            processor.exec(ctx)
            ctx.toResponse()
        }
    }
}