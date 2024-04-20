package ru.otus.osms.repo.stubs

import ru.otus.osms.common.repo.*
import ru.otus.osms.stubs.OsmsBookingStub

class BookingRepoStub() : IBookingRepository {
    override suspend fun createBooking(request: DbBookingRequest): DbBookingResponse {
        return DbBookingResponse(
            data = OsmsBookingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readBooking(request: DbBookingUidRequest): DbBookingResponse {
        return DbBookingResponse(
            data = OsmsBookingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateBooking(request: DbBookingRequest): DbBookingResponse {
        return DbBookingResponse(
            data = OsmsBookingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteBooking(request: DbBookingUidRequest): DbBookingResponse {
        return DbBookingResponse(
            data = OsmsBookingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchBooking(request: DbBookingFilterRequest): DbBookingsResponse {
        return DbBookingsResponse(
            data = OsmsBookingStub.prepareSearchList(),
            isSuccess = true,
        )
    }
}
