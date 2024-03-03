package ru.otus.osms.ktor.jvm.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.osms.app.common.controllerHelper
import ru.otus.osms.api.v1.models.IRequest
import ru.otus.osms.api.v1.models.IResponse
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.mappers.jackson.v1.fromTransport
import ru.otus.osms.mappers.jackson.v1.toTransportBooking
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: OsmsAppSettings,
    clazz: KClass<*>,
    logUid: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    {
        respond(toTransportBooking())
    },
    clazz,
    logUid,
)