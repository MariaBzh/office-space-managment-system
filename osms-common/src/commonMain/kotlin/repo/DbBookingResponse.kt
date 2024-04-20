package ru.otus.osms.common.repo

import ru.otus.osms.common.helpers.errorRepoConcurrency
import ru.otus.osms.common.helpers.errorEmptyId as osmsErrorEmptyId
import ru.otus.osms.common.helpers.errorNotFound as osmsErrorNotFound
import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsBookingLock
import ru.otus.osms.common.models.OsmsError

data class DbBookingResponse(
    override val data: OsmsBooking?,
    override val isSuccess: Boolean,
    override val errors: List<OsmsError> = emptyList()
): IDbResponse<OsmsBooking> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbBookingResponse(null, true)
        fun success(result: OsmsBooking) = DbBookingResponse(result, true)
        fun error(errors: List<OsmsError>, data: OsmsBooking? = null) = DbBookingResponse(data, false, errors)
        fun error(error: OsmsError, data: OsmsBooking? = null) = DbBookingResponse(data, false, listOf(error))

        val errorEmptyId = error(osmsErrorEmptyId)

        fun errorConcurrent(lock: OsmsBookingLock, booking: OsmsBooking?) = error(
            errorRepoConcurrency(lock, booking?.lock?.let { OsmsBookingLock(it.asString()) }),
            booking
        )

        val errorNotFound = error(osmsErrorNotFound)
    }
}
