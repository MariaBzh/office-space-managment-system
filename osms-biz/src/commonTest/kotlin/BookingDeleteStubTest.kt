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

        val context = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingRequest = BOOKING_REQUEST,
        )
        
        PROCESSOR.exec(context)

        val stub = OsmsBookingStub.get()

        assertEquals(stub.bookingUid, context.bookingResponse.bookingUid)
        assertEquals(stub.userUid, context.bookingResponse.userUid)
        assertEquals(stub.workspaceUid, context.bookingResponse.workspaceUid)

        assertEquals(stub.branch.branchUid, context.bookingResponse.branch.branchUid)
        assertEquals(stub.branch.name, context.bookingResponse.branch.name)

        assertEquals(stub.floor.floorUid, context.bookingResponse.floor.floorUid)
        assertEquals(stub.floor.level, context.bookingResponse.floor.level)

        assertEquals(stub.office.officeUid, context.bookingResponse.office.officeUid)
        assertEquals(stub.office.name, context.bookingResponse.office.name)

        assertEquals(stub.startTime, context.bookingResponse.startTime)
        assertEquals(stub.endTime, context.bookingResponse.endTime)

        assertContains(stub.permissions, OsmsBookingPermissions.READ)
        assertContains(stub.permissions, OsmsBookingPermissions.UPDATE)
        assertContains(stub.permissions, OsmsBookingPermissions.DELETE)
    }

    @Test
    fun badUid() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_UID,
            bookingResponse = BOOKING_REQUEST,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(bookingUid = BOOKING_UID), context.bookingResponse)

        assertEquals("bad_uid", context.errors.firstOrNull()?.code)
        assertEquals("Validation", context.errors.firstOrNull()?.group)
        assertEquals("'bookingUid'", context.errors.firstOrNull()?.field)
        assertEquals("Incorrect UID", context.errors.firstOrNull()?.message)
    }

    @Test
    fun notFound() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NOT_FOUND,
            bookingRequest = BOOKING_REQUEST,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("not_found", context.errors.firstOrNull()?.code)
        assertEquals("Booking not found", context.errors.firstOrNull()?.message)
    }

    @Test
    fun databaseError() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.DB_ERROR,
            bookingResponse = BOOKING_REQUEST,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(bookingUid = BOOKING_UID), context.bookingResponse)

        assertEquals("internal-db", context.errors.firstOrNull()?.code)
        assertEquals("internal", context.errors.firstOrNull()?.group)
        assertEquals("Internal error", context.errors.firstOrNull()?.message)
    }

    @Test
    fun badNoCase() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.DELETE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NONE,
            bookingRequest = BOOKING_REQUEST,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("validation", context.errors.firstOrNull()?.code)
        assertEquals("validation", context.errors.firstOrNull()?.group)
        assertEquals("Wrong stub case is requested: ${OsmsState.NONE.name}", context.errors.firstOrNull()?.message)
    }

    companion object {
        private val PROCESSOR = OsmsBookingProcessor()
        private val BOOKING_UID = OsmsBookingUid("booking-3000")
        private val BOOKING_REQUEST = OsmsBooking(
            bookingUid = BOOKING_UID
        )
    }
}