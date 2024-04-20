package ru.otus.osms.mappers.kmp.v1.test

import ru.otus.osms.api.v1.kpm.models.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.mappers.kmp.v1.fromTransport
import ru.otus.osms.mappers.kmp.v1.toTransportBooking
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val request = BookingDeleteRequest(
            requestUid  = REQUEST_UID,
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingDeleteObject(
                bookingUid = BOOKING_UID,
                lock = LOCK_UID,
            ),
        )
        val context = OsmsContext()
        
        context.fromTransport(request)

        assertEquals(OsmsStub.SUCCESS, context.stubCase)
        assertEquals(OsmsWorkMode.STUB, context.workMode)
        assertEquals(BOOKING_UID, context.bookingRequest.bookingUid.asString())
        assertEquals(LOCK_UID, context.bookingRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = OsmsContext(
            requestUid = OsmsRequestUid(REQUEST_UID),
            command = OsmsCommand.DELETE,
            bookingRequest = OsmsBooking(
                bookingUid = OsmsBookingUid("!@#/$%^&*"),
                lock = OsmsBookingLock(LOCK_UID),
            ),
            errors = mutableListOf(
                OsmsError(
                    code = "err",
                    group = "request",
                    field = "uid",
                    message = "wrong uid",
                )
            ),
            state = OsmsState.RUNNING,
        )
        val request = context.toTransportBooking() as BookingDeleteResponse

        assertEquals(REQUEST_UID, request.requestUid)
        assertEquals(1, request.errors?.size)
        assertEquals(context.errors.firstOrNull()?.code, request.errors?.firstOrNull()?.code)
        assertEquals(context.errors.firstOrNull()?.group, request.errors?.firstOrNull()?.group)
        assertEquals(context.errors.firstOrNull()?.field, request.errors?.firstOrNull()?.field)
        assertEquals(context.errors.firstOrNull()?.message, request.errors?.firstOrNull()?.message)
    }

    companion object {
        private const val REQUEST_UID = "request-1"
        private const val BOOKING_UID = "booking-1"
        private const val LOCK_UID = "lock-1"
    }
}