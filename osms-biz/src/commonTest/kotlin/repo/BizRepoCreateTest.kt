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
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BizRepoCreateTest {
    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = OsmsContext(
            command = COMMAND,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.TEST,
            bookingRequest = OsmsBooking(
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
            ),
        )

        PROCESSOR.exec(context)
        
        assertEquals(OsmsState.FINISHING, context.state)
        assertTrue { context.errors.isEmpty() }
        assertEquals(UUID, context.bookingResponse.bookingUid.asString())
        assertEquals(USER_UID.asString(), context.bookingResponse.userUid.asString())
        assertEquals(UUID, context.bookingResponse.workspaceUid.asString())
        assertEquals(UUID, context.bookingResponse.branch.branchUid.asString())
        assertEquals("Московский", context.bookingResponse.branch.name)
        assertEquals(UUID, context.bookingResponse.floor.floorUid.asString())
        assertEquals("1A", context.bookingResponse.floor.level)
        assertEquals(UUID, context.bookingResponse.office.officeUid.asString())
        assertEquals("Марс", context.bookingResponse.office.name)
        assertEquals("2024-01-01T10:00:00", context.bookingResponse.startTime)
        assertEquals("2024-01-01T12:00:00", context.bookingResponse.endTime)
    }
    
    companion object {
        private val USER_UID = OsmsUserUid("user-1")
        private val COMMAND = OsmsCommand.CREATE

        private const val UUID = "2d0ff2ad-11se-df12-9y0q-we11a0xz45g1"

        private val REPO = BookingRepositoryMock(
            invokeCreateBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = OsmsBooking(
                        bookingUid = it.booking.bookingUid,
                        userUid = it.booking.userUid,
                        branch = it.booking.branch,
                        floor = it.booking.floor,
                        office = it.booking.office,
                        workspaceUid = it.booking.workspaceUid,
                        startTime = it.booking.startTime,
                        endTime = it.booking.endTime,
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
