package ru.otus.osms.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.osms.ktor.OsmsAppSettings

fun Route.v1Booking(appSettings: OsmsAppSettings) {
    route("bookings") {
        post("create") {
            call.createBooking(appSettings)
        }
        post("read") {
            call.readBooking(appSettings)
        }
        post("update") {
            call.updateBooking(appSettings)
        }
        post("delete") {
            call.deleteBooking(appSettings)
        }
        post("search") {
            call.searchBooking(appSettings)
        }
    }
}
