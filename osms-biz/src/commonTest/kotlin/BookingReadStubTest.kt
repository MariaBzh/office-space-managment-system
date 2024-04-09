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

        val context = OsmsContext(
            command = OsmsCommand.READ,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingResponse = BOOKING_REQUEST,
        )
        
        PROCESSOR.exec(context)
        
        with (OsmsBookingStub.get()) {
            assertEquals(bookingUid, context.bookingResponse.bookingUid)
            assertEquals(userUid, context.bookingResponse.userUid)
            assertEquals(workspaceUid, context.bookingResponse.workspaceUid)

            assertEquals(branch.branchUid, context.bookingResponse.branch.branchUid)
            assertEquals(branch.name, context.bookingResponse.branch.name)

            assertEquals(floor.floorUid, context.bookingResponse.floor.floorUid)
            assertEquals(floor.level, context.bookingResponse.floor.level)

            assertEquals(office.officeUid, context.bookingResponse.office.officeUid)
            assertEquals(office.name, context.bookingResponse.office.name)

            assertEquals(startTime, context.bookingResponse.startTime)
            assertEquals(endTime, context.bookingResponse.endTime)

            assertContains(permissions, OsmsBookingPermissions.READ)
            assertContains(permissions, OsmsBookingPermissions.UPDATE)
            assertContains(permissions, OsmsBookingPermissions.DELETE)
        }
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