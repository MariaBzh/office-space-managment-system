package ru.otus.osms.ktor.v1

import io.ktor.server.application.*
import ru.otus.osms.api.v1.kpm.models.*
import ru.otus.osms.ktor.OsmsAppSettings
import kotlin.reflect.KClass

val kclCreate: KClass<*> = ApplicationCall::createBooking::class
val kclRead: KClass<*> = ApplicationCall::readBooking::class
val kclUpdate: KClass<*> = ApplicationCall::updateBooking::class
val kclDelete: KClass<*> = ApplicationCall::deleteBooking::class
val kclSearch: KClass<*> = ApplicationCall::searchBooking::class

const val CREATE = "create"
const val READ = "read"
const val UPDATE = "update"
const val DELETE = "delete"
const val SEARCH = "search"

suspend fun ApplicationCall.createBooking(appSettings: OsmsAppSettings) =
    processV1<BookingCreateRequest, BookingCreateResponse>(appSettings, kclCreate, CREATE)

suspend fun ApplicationCall.readBooking(appSettings: OsmsAppSettings) =
    processV1<BookingReadRequest, BookingReadResponse>(appSettings, kclRead, READ)

suspend fun ApplicationCall.updateBooking(appSettings: OsmsAppSettings) =
    processV1<BookingUpdateRequest, BookingUpdateResponse>(appSettings, kclUpdate, UPDATE)

suspend fun ApplicationCall.deleteBooking(appSettings: OsmsAppSettings) =
    processV1<BookingDeleteRequest, BookingDeleteResponse>(appSettings, kclDelete, DELETE)

suspend fun ApplicationCall.searchBooking(appSettings: OsmsAppSettings) =
    processV1<BookingSearchRequest, BookingSearchResponse>(appSettings, kclSearch, SEARCH)
