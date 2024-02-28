package ru.otus.osms.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.osms.biz.OsmsBookingProcessor

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
