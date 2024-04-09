package ru.otus.osms.common.repo

import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsError

data class DbBookingResponse(
    override val data: OsmsBooking?,
    override val isSuccess: Boolean,
    override val errors: List<OsmsError> = emptyList()
): IDbResponse<OsmsBooking> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbBookingResponse(null, true)
        fun success(result: OsmsBooking) = DbBookingResponse(result, true)
        fun error(errors: List<OsmsError>) = DbBookingResponse(null, false, errors)
        fun error(error: OsmsError) = DbBookingResponse(null, false, listOf(error))
    }
}
