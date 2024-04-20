package ru.otus.osms.common.repo

import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsError

data class DbBookingsResponse(
    override val data: List<OsmsBooking>?,
    override val isSuccess: Boolean,
    override val errors: List<OsmsError> = emptyList(),
): IDbResponse<List<OsmsBooking>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbBookingsResponse(emptyList(), true)
        fun success(result: List<OsmsBooking>) = DbBookingsResponse(result, true)
        fun error(errors: List<OsmsError>) = DbBookingsResponse(null, false, errors)
        fun error(error: OsmsError) = DbBookingsResponse(null, false, listOf(error))
    }
}
