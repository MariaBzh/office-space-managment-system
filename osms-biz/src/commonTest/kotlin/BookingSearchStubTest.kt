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
        val ctx = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(ctx)

        assertTrue(ctx.bookingsResponse.size >= 1)

        val first = ctx.bookingsResponse.firstOrNull() ?: fail("Empty response list")

        assertEquals(first.userUid.asString(), BOOKING_FILTER.userUid.asString())
    }

    @Test
    fun notFound() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NOT_FOUND,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("not_found", ctx.errors.firstOrNull()?.code)
        assertEquals("Booking not found", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun badUid() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_UID,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("bad_uid", ctx.errors.firstOrNull()?.code)
        assertEquals("Validation", ctx.errors.firstOrNull()?.group)
        assertEquals("'bookingUid'", ctx.errors.firstOrNull()?.field)
        assertEquals("Incorrect UID", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun badTime() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_TIME,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("bad_time", ctx.errors.firstOrNull()?.code)
        assertEquals("Validation", ctx.errors.firstOrNull()?.group)
        assertEquals("'startTime' or 'endTime'", ctx.errors.firstOrNull()?.field)
        assertEquals("Incorrect time", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.DB_ERROR,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
        assertEquals("Internal error", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.SEARCH,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NONE,
            bookingFilterRequest = BOOKING_FILTER,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("validation", ctx.errors.firstOrNull()?.code)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
        assertEquals("Wrong stub case is requested: ${OsmsState.NONE.name}", ctx.errors.firstOrNull()?.message)
    }

    companion object {
        private val PROCESSOR = OsmsBookingProcessor()
        private val USER_UID = OsmsUserUid("user-1")
        private val BOOKING_FILTER = OsmsBookingSearchFilter(
            userUid = USER_UID
        )
    }
}