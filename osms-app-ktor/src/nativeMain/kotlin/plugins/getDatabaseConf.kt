package ru.otus.osms.ktor.plugins

import io.ktor.server.application.*
import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.db.inmemory.BookingRepoInMemory

actual fun Application.getDatabaseConf(type: BookingDbType): IBookingRepository {
    return BookingRepoInMemory()
}
