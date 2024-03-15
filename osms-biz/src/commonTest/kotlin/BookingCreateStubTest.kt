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

class BookingCreateStubTest {
    @Test
    fun create() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.CREATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.SUCCESS,
            bookingRequest = BOOKING,
        )

        PROCESSOR.exec(ctx)

        assertEquals(OsmsBookingStub.get().bookingUid, ctx.bookingResponse.bookingUid)
        assertEquals(USER_UID, ctx.bookingResponse.userUid)
        assertEquals(WORKPLACE_UID, ctx.bookingResponse.workspaceUid)

        assertEquals(BRANCH.branchUid, ctx.bookingResponse.branch.branchUid)
        assertEquals(BRANCH.name, ctx.bookingResponse.branch.name)

        assertEquals(FLOOR.floorUid, ctx.bookingResponse.floor.floorUid)
        assertEquals(FLOOR.level, ctx.bookingResponse.floor.level)

        assertEquals(OFFICE.officeUid, ctx.bookingResponse.office.officeUid)
        assertEquals(OFFICE.name, ctx.bookingResponse.office.name)

        assertEquals(START_TIME, ctx.bookingResponse.startTime)
        assertEquals(END_TIME, ctx.bookingResponse.endTime)

        assertContains(ctx.bookingResponse.permissions, OsmsBookingPermissions.READ)
        assertContains(ctx.bookingResponse.permissions, OsmsBookingPermissions.UPDATE)
        assertContains(ctx.bookingResponse.permissions, OsmsBookingPermissions.DELETE)
    }

    @Test
    fun badUid() = runTest {
        val ctx = OsmsContext(
            command = OsmsCommand.CREATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_UID,
            bookingRequest = BOOKING,
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
            command = OsmsCommand.CREATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.BAD_TIME,
            bookingRequest = BOOKING,
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
            command = OsmsCommand.CREATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.DB_ERROR,
            bookingRequest = BOOKING,
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
            command = OsmsCommand.CREATE,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.STUB,
            stubCase = OsmsStub.NONE,
            bookingRequest = BOOKING,
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