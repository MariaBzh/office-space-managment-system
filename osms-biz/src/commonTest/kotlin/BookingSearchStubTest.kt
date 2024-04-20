package ru.otus.osms.biz.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import kotlin.test.*

class BookingSearchStubTest {
    @Test
    fun search() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(context)

        assertTrue(context.bookingsResponse.size >= 1)

        val first = context.bookingsResponse.firstOrNull() ?: fail("Empty response list")

        assertEquals(first.userUid.asString(), BOOKING_FILTER.userUid.asString())
    }

    @Test
    fun notFound() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NOT_FOUND,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("not_found", context.errors.firstOrNull()?.code)
        assertEquals("Booking not found", context.errors.firstOrNull()?.message)
    }

    @Test
    fun badUid() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_UID,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("bad_uid", context.errors.firstOrNull()?.code)
        assertEquals("Validation", context.errors.firstOrNull()?.group)
        assertEquals("'bookingUid'", context.errors.firstOrNull()?.field)
        assertEquals("Incorrect UID", context.errors.firstOrNull()?.message)
    }

    @Test
    fun badTime() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_TIME,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("bad_time", context.errors.firstOrNull()?.code)
        assertEquals("Validation", context.errors.firstOrNull()?.group)
        assertEquals("'startTime' or 'endTime'", context.errors.firstOrNull()?.field)
        assertEquals("Incorrect time", context.errors.firstOrNull()?.message)
    }

    @Test
    fun databaseError() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.DB_ERROR,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("internal-db", context.errors.firstOrNull()?.code)
        assertEquals("internal", context.errors.firstOrNull()?.group)
        assertEquals("Internal error", context.errors.firstOrNull()?.message)
    }

    @Test
    fun badNoCase() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NONE,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("validation", context.errors.firstOrNull()?.code)
        assertEquals("validation", context.errors.firstOrNull()?.group)
        assertEquals("Wrong stub case is requested: ${OsmsState.NONE.name}", context.errors.firstOrNull()?.message)
    }

    companion object {
        private val PROCESSOR = OsmsBookingProcessor()
        private val USER_UID = OsmsUserUid("user-1")
        private val BOOKING_FILTER = OsmsBookingSearchFilter(
            userUid = USER_UID
        )
    }
}