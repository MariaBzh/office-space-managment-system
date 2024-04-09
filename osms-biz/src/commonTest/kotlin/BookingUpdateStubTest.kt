package ru.otus.osms.biz.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class BookingUpdateStubTest {
    @Test
    fun update() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.UPDATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingResponse = BOOKING,
        )
        
        PROCESSOR.exec(context)

        assertEquals(BOOKING.bookingUid, context.bookingResponse.bookingUid)
        assertEquals(USER_UID, context.bookingResponse.userUid)
        assertEquals(WORKPLACE_UID, context.bookingResponse.workspaceUid)

        assertEquals(BRANCH.branchUid, context.bookingResponse.branch.branchUid)
        assertEquals(BRANCH.name, context.bookingResponse.branch.name)

        assertEquals(FLOOR.floorUid, context.bookingResponse.floor.floorUid)
        assertEquals(FLOOR.level, context.bookingResponse.floor.level)

        assertEquals(OFFICE.officeUid, context.bookingResponse.office.officeUid)
        assertEquals(OFFICE.name, context.bookingResponse.office.name)

        assertEquals(START_TIME, context.bookingResponse.startTime)
        assertEquals(END_TIME, context.bookingResponse.endTime)

        assertContains(context.bookingResponse.permissions, OsmsBookingPermissions.READ)
        assertContains(context.bookingResponse.permissions, OsmsBookingPermissions.UPDATE)
        assertContains(context.bookingResponse.permissions, OsmsBookingPermissions.DELETE)
    }

    @Test
    fun badUid() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.UPDATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_UID,
            bookingResponse = BOOKING,
        )

        PROCESSOR.exec(context)

        assertEquals(BOOKING, context.bookingResponse)

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
            bookingResponse = BOOKING,
        )

        PROCESSOR.exec(context)

        assertEquals(BOOKING, context.bookingResponse)

        assertEquals("bad_time", context.errors.firstOrNull()?.code)
        assertEquals("Validation", context.errors.firstOrNull()?.group)
        assertEquals("'startTime' or 'endTime'", context.errors.firstOrNull()?.field)
        assertEquals("Incorrect time", context.errors.firstOrNull()?.message)
    }

    @Test
    fun notFound() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.UPDATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NOT_FOUND,
            bookingRequest = BOOKING,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsBooking(), context.bookingResponse)

        assertEquals("not_found", context.errors.firstOrNull()?.code)
        assertEquals("Booking not found", context.errors.firstOrNull()?.message)
    }

    @Test
    fun databaseError() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.UPDATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.DB_ERROR,
            bookingResponse = BOOKING,
        )

        PROCESSOR.exec(context)

        assertEquals(BOOKING, context.bookingResponse)

        assertEquals("internal-db", context.errors.firstOrNull()?.code)
        assertEquals("internal", context.errors.firstOrNull()?.group)
        assertEquals("Internal error", context.errors.firstOrNull()?.message)
    }

    @Test
    fun badNoCase() = runTest {
        val context = OsmsContext(
            command = OsmsCommand.UPDATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NONE,
            bookingRequest = BOOKING,
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
        private val USER_UID = OsmsUserUid("user-1")
        private val WORKPLACE_UID = OsmsWorkspaceUid("workspace-1")
        private val BRANCH = OsmsBranch(
            branchUid = OsmsBranchUid("branch-1"),
            name = "A",
        )
        private val FLOOR = OsmsFloor(
            floorUid = OsmsFloorUid("floor-1"),
            level = "1",
        )
        private val OFFICE = OsmsOffice(
            officeUid = OsmsOfficeUid("office-1"),
            name = "Московский"
        )
        private const val START_TIME = "2024-01-01 10:00:00.0000"
        private const val END_TIME = "2024-01-01 11:00:00.0000"
        private val PERMISSIONS = mutableSetOf(
            OsmsBookingPermissions.READ,
            OsmsBookingPermissions.UPDATE,
            OsmsBookingPermissions.DELETE,
        )
        private val BOOKING = OsmsBooking(
            bookingUid = BOOKING_UID,
            userUid = USER_UID,
            workspaceUid = WORKPLACE_UID,
            branch = BRANCH,
            floor = FLOOR,
            office = OFFICE,
            startTime = START_TIME,
            endTime = END_TIME,
            permissions = PERMISSIONS,
        )
    }
}