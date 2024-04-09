package ru.otus.osms.biz.test.repo

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingsResponse
import ru.otus.osms.repo.test.BookingRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {
    @Test
    fun repoSearchSuccessTest() = runTest {
        val context = OsmsContext(
            command = COMMAND,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.TEST,
            bookingFilterRequest = OsmsBookingSearchFilter(
                userUid = USER_UID
            ),
        )
        
        PROCESSOR.exec(context)
        
        assertEquals(OsmsState.FINISHING, context.state)
        assertEquals(1, context.bookingsResponse.size)
    }

    companion object {
        private val USER_UID = OsmsUserUid("user-1")
        private val COMMAND = OsmsCommand.SEARCH

        private const val UUID = "2d0ff2ad-11se-df12-9y0q-we11a0xz45g1"

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
        private val REPO by lazy {
            BookingRepositoryMock(
                invokeSearchBooking = {
                    DbBookingsResponse(
                        isSuccess = true,
                        data = listOf(INIT_BOOKING),
                    )
                },
            )
        }
        private val SETTINGS by lazy {
            OsmsCorSettings(
                repoTest = REPO
            )
        }
        private val PROCESSOR by lazy {
            OsmsBookingProcessor(SETTINGS)
        }
    }
}
