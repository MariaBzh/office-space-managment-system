package ru.otus.osms.repo.test

import ru.otus.osms.common.repo.*

class BookingRepositoryMock(
    private val invokeCreateBooking: (DbBookingRequest) -> DbBookingResponse = { DbBookingResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadBooking: (DbBookingUidRequest) -> DbBookingResponse = { DbBookingResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateBooking: (DbBookingRequest) -> DbBookingResponse = { DbBookingResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteBooking: (DbBookingUidRequest) -> DbBookingResponse = { DbBookingResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchBooking: (DbBookingFilterRequest) -> DbBookingsResponse = { DbBookingsResponse.MOCK_SUCCESS_EMPTY },
): IBookingRepository {
    override suspend fun createBooking(request: DbBookingRequest): DbBookingResponse {
        return invokeCreateBooking(request)
    }

    override suspend fun readBooking(request: DbBookingUidRequest): DbBookingResponse {
        return invokeReadBooking(request)
    }

    override suspend fun updateBooking(request: DbBookingRequest): DbBookingResponse {
        return invokeUpdateBooking(request)
    }

    override suspend fun deleteBooking(request: DbBookingUidRequest): DbBookingResponse {
        return invokeDeleteBooking(request)
    }

    override suspend fun searchBooking(request: DbBookingFilterRequest): DbBookingsResponse {
        return invokeSearchBooking(request)
    }
}
