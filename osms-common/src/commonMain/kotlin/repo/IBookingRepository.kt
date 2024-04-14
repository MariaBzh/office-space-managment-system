package ru.otus.osms.common.repo

interface IBookingRepository {
    suspend fun createBooking(request: DbBookingRequest): DbBookingResponse
    suspend fun readBooking(request: DbBookingUidRequest): DbBookingResponse
    suspend fun updateBooking(request: DbBookingRequest): DbBookingResponse
    suspend fun deleteBooking(request: DbBookingUidRequest): DbBookingResponse
    suspend fun searchBooking(request: DbBookingFilterRequest): DbBookingsResponse
    companion object {
        val NONE = object : IBookingRepository {
            override suspend fun createBooking(request: DbBookingRequest): DbBookingResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readBooking(request: DbBookingUidRequest): DbBookingResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateBooking(request: DbBookingRequest): DbBookingResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteBooking(request: DbBookingUidRequest): DbBookingResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchBooking(request: DbBookingFilterRequest): DbBookingsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}