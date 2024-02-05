package ru.otus.osms.ktor.jvm.v1

import OsmsBookingProcessor
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1Booking(processor: OsmsBookingProcessor) {
    route("bookings") {
        post("create") {
            call.createBooking(processor)
        }
        post("read") {
            call.readBooking(processor)
        }
        post("update") {
            call.updateBooking(processor)
        }
        post("delete") {
            call.deleteBooking(processor)
        }
        post("search") {
            call.searchBooking(processor)
        }
    }
}
