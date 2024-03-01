package ru.otus.osms.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.osms.app.common.controllerHelper
import ru.otus.osms.api.v1.kpm.models.IRequest
import ru.otus.osms.api.v1.kpm.models.IResponse
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.mappers.kmp.v1.fromTransport
import ru.otus.osms.mappers.kmp.v1.toTransportBooking

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: OsmsAppSettings,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    {
        respond(toTransportBooking())
    }
)