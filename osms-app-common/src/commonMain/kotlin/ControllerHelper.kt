package ru.osms.app.common

import kotlinx.datetime.Clock
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.asOsmsError
import ru.otus.osms.common.models.OsmsState

suspend inline fun <T> IOsmsAppSettings.controllerHelper(
    getRequest: OsmsContext.() -> Unit,
    toResponse: OsmsContext.() -> T,
): T {
    val ctx = OsmsContext(
        timeStart = Clock.System.now(),
    )

    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = OsmsState.FAILING
        ctx.errors.add(e.asOsmsError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}