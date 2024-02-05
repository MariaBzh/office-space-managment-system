package ru.otus.osms.ktor.jvm.v1

import OsmsBookingProcessor
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.osms.api.v1.models.*
import ru.otus.osms.common.*
import ru.otus.osms.mappers.jackson.v1.*

suspend fun ApplicationCall.createBooking(processor: OsmsBookingProcessor) {
    val request = receive<BookingCreateRequest>()
    val context = OsmsContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.readBooking(processor: OsmsBookingProcessor) {
    val request = receive<BookingReadRequest>()
    val context = OsmsContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.updateBooking(processor: OsmsBookingProcessor) {
    val request = receive<BookingUpdateRequest>()
    val context = OsmsContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteBooking(processor: OsmsBookingProcessor) {
    val request = receive<BookingDeleteRequest>()
    val context = OsmsContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchBooking(processor: OsmsBookingProcessor) {
    val request = receive<BookingSearchRequest>()
    val context = OsmsContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportSearch())
}
