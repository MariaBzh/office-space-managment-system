package ru.otus.osms.biz.test.repo

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingResponse
import ru.otus.osms.repo.test.BookingRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoUpdateTest {
    @Test
    fun repoUpdateSuccessTest() = runTest {
        val bookingToUpdate = OsmsBooking(
            bookingUid = OsmsBookingUid(UUID),
            userUid = USER_UID,
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(UUID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(UUID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(UUID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(UUID),
            startTime = START_TIME_UPDATE,
            endTime = END_TIME_UPDATE,
        )
        val context = OsmsContext(
            command = COMMAND,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.TEST,
            bookingRequest = bookingToUpdate,
        )

        PROCESSOR.exec(context)

        assertEquals(OsmsState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(bookingToUpdate.bookingUid, context.bookingResponse.bookingUid)
        assertEquals(bookingToUpdate.userUid, context.bookingResponse.userUid)
        assertEquals(bookingToUpdate.workspaceUid, context.bookingResponse.workspaceUid)
        assertEquals(bookingToUpdate.branch.branchUid, context.bookingResponse.branch.branchUid)
        assertEquals(bookingToUpdate.branch.name, context.bookingResponse.branch.name)
        assertEquals(bookingToUpdate.floor.floorUid, context.bookingResponse.floor.floorUid)
        assertEquals(bookingToUpdate.floor.level, context.bookingResponse.floor.level)
        assertEquals(bookingToUpdate.office.officeUid, context.bookingResponse.office.officeUid)
        assertEquals(bookingToUpdate.office.name, context.bookingResponse.office.name)
        assertEquals(bookingToUpdate.startTime, context.bookingResponse.startTime)
        assertEquals(bookingToUpdate.endTime, context.bookingResponse.endTime)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(COMMAND)

    companion object {
        private val USER_UID = OsmsUserUid("user-1")
        private val COMMAND = OsmsCommand.UPDATE

        private const val UUID = "2d0ff2booking-11se-df12-9y0q-we11a0xz45g1"
        private const val START_TIME_UPDATE = "2024-01-01T11:00:00"
        private const val END_TIME_UPDATE = "2024-01-01T13:00:00"

        private val INIT_BOOKING = OsmsBooking(
            bookingUid = OsmsBookingUid(UUID),
            userUid = USER_UID,
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(UUID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(UUID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(UUID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(UUID),
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
        )
        private val REPO = BookingRepositoryMock(
            invokeReadBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = INIT_BOOKING,
                )
            },
            invokeUpdateBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = OsmsBooking(
                        bookingUid = it.booking.bookingUid,
                        userUid = it.booking.userUid,
                        branch = it.booking.branch,
                        floor = it.booking.floor,
                        office = it.booking.office,
                        workspaceUid = it.booking.workspaceUid,
                        startTime = START_TIME_UPDATE,
                        endTime = END_TIME_UPDATE,
                        permissions = it.booking.permissions,
                    )
                )
            }
        )
        private val SETTINGS = OsmsCorSettings(
            repoTest = REPO
        )
        private val PROCESSOR = OsmsBookingProcessor(SETTINGS)
    }
}