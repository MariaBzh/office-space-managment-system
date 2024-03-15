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

class BookingDeleteStubTest {
    @Test
    fun delete() = runTest {

        val ctx = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingRequest = BOOKING_REQUEST,
        )
        
        PROCESSOR.exec(ctx)

        val stub = OsmsBookingStub.get()

        assertEquals(stub.bookingUid, ctx.bookingResponse.bookingUid)
        assertEquals(stub.userUid, ctx.bookingResponse.userUid)
        assertEquals(stub.workspaceUid, ctx.bookingResponse.workspaceUid)

        assertEquals(stub.branch.branchUid, ctx.bookingResponse.branch.branchUid)
        assertEquals(stub.branch.name, ctx.bookingResponse.branch.name)

        assertEquals(stub.floor.floorUid, ctx.bookingResponse.floor.floorUid)
        assertEquals(stub.floor.level, ctx.bookingResponse.floor.level)

        assertEquals(stub.office.officeUid, ctx.bookingResponse.office.officeUid)
        assertEquals(stub.office.name, ctx.bookingResponse.office.name)

        assertEquals(stub.startTime, ctx.bookingResponse.startTime)
        assertEquals(stub.endTime, ctx.bookingResponse.endTime)

        assertContains(stub.permissions, OsmsBookingPermissions.READ)
        assertContains(stub.permissions, OsmsBookingPermissions.UPDATE)
        assertContains(stub.permissions, OsmsBookingPermissions.DELETE)
    }

    @Test
    fun badUid() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_UID,
            bookingResponse = BOOKING_REQUEST,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBooking(bookingUid = BOOKING_UID), ctx.bookingResponse)

        assertEquals("bad_uid", ctx.errors.firstOrNull()?.code)
        assertEquals("Validation", ctx.errors.firstOrNull()?.group)
        assertEquals("'bookingUid'", ctx.errors.firstOrNull()?.field)
        assertEquals("Incorrect UID", ctx.errors.firstOrNull()?.message)
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