package ru.otus.osms.ktor.jvm.v1

import io.ktor.server.application.*
import ru.otus.osms.api.v1.models.*
import ru.otus.osms.ktor.OsmsAppSettings

suspend fun ApplicationCall.createBooking(appSettings: OsmsAppSettings) =
    processV1<BookingCreateRequest, BookingCreateResponse>(appSettings)

suspend fun ApplicationCall.readBooking(appSettings: OsmsAppSettings) =
    processV1<BookingReadRequest, BookingReadResponse>(appSettings)

suspend fun ApplicationCall.updateBooking(appSettings: OsmsAppSettings) =
    processV1<BookingUpdateRequest, BookingUpdateResponse>(appSettings)

suspend fun ApplicationCall.deleteBooking(appSettings: OsmsAppSettings) =
    processV1<BookingDeleteRequest, BookingDeleteResponse>(appSettings)

suspend fun ApplicationCall.searchBooking(appSettings: OsmsAppSettings) =
    processV1<BookingSearchRequest, BookingSearchResponse>(appSettings)
