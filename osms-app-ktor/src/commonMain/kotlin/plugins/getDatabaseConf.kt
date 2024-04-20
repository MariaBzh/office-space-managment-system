package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.common.repo.IBookingRepository

expect fun Application.getDatabaseConf(type: BookingDbType): IBookingRepository

enum class BookingDbType(val confName: String) {
    PROD("prod"), TEST("test")
}
