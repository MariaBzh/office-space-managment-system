package ru.otus.osms.biz.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.stubs.OsmsBookingStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class BookingReadStubTest {
    @Test
    fun read() = runTest {

        val ctx = OsmsContext(
            command = OsmsCommand.READ,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingResponse = BOOKING_REQUEST,
        )
        
        PROCESSOR.exec(ctx)
        
        with (OsmsBookingStub.get()) {
            assertEquals(bookingUid, ctx.bookingResponse.bookingUid)
            assertEquals(userUid, ctx.bookingResponse.userUid)
            assertEquals(workspaceUid, ctx.bookingResponse.workspaceUid)

            assertEquals(branch.branchUid, ctx.bookingResponse.branch.branchUid)
            assertEquals(branch.name, ctx.bookingResponse.branch.name)

            assertEquals(floor.floorUid, ctx.bookingResponse.floor.floorUid)
            assertEquals(floor.level, ctx.bookingResponse.floor.level)

            assertEquals(office.officeUid, ctx.bookingResponse.office.officeUid)
            assertEquals(office.name, ctx.bookingResponse.office.name)

            assertEquals(startTime, ctx.bookingResponse.startTime)
            assertEquals(endTime, ctx.bookingResponse.endTime)

            assertContains(permissions, OsmsBookingPermissions.READ)
            assertContains(permissions, OsmsBookingPermissions.UPDATE)
            assertContains(permissions, OsmsBookingPermissions.DELETE)
        }
    }

    @Test
    fun notFound() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NOT_FOUND,
            bookingRequest = BOOKING_REQUEST,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("not_found", ctx.errors.firstOrNull()?.code)
        assertEquals("Booking not found", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.DB_ERROR,
            bookingResponse = BOOKING_REQUEST,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(bookingUid = BOOKING_UID), ctx.bookingResponse)

        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
        assertEquals("Internal error", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NONE,
            bookingRequest = BOOKING_REQUEST,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(), ctx.bookingResponse)

        assertEquals("validation", ctx.errors.firstOrNull()?.code)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
        assertEquals("Wrong stub case is requested: ${OsmsState.NONE.name}", ctx.errors.firstOrNull()?.message)
    }

    companion object {
        private val PROCESSOR = OsmsBookingProcessor()
        private val BOOKING_UID = OsmsBookingUid("booking-3000")
        private val BOOKING_REQUEST = OsmsBooking(
            bookingUid = BOOKING_UID
        )
    }
}