package ru.otus.osms.common.repo

import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsBookingUid

data class DbBookingUidRequest(
    var bookingUid: OsmsBookingUid,
) {
    constructor(booking: OsmsBooking): this(booking.bookingUid)
}
