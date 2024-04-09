package ru.otus.osms.repo.test

import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingUpdateTest {
    abstract val repo: IBookingRepository
    protected open val updateSuccess = initObjects[0]
    private val updateUidNotFound = OsmsBookingUid("booking-2")

    private val reqUpdateSucc by lazy {
        OsmsBooking(
            bookingUid = updateSuccess.bookingUid,
            userUid = OsmsUserUid("user-1"),
            workspaceUid = OsmsWorkspaceUid("workspace-1"),
            branch = OsmsBranch(OsmsBranchUid("branch-1"),"Branch"),
            floor = OsmsFloor(OsmsFloorUid("floor-1"), "1","Floor"),
            office = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
            description = "Init test model",
            startTime = "2024-01-01T10:00:00.0000",
            endTime = "2024-01-01T12:00:00.0000",
        )
    }
    private val reqUpdateNotFound = OsmsBooking(
        bookingUid = updateUidNotFound,
        userUid = OsmsUserUid("user-1"),
        workspaceUid = OsmsWorkspaceUid("workspace-1"),
        branch = OsmsBranch(OsmsBranchUid("branch-1"),"Branch"),
        floor = OsmsFloor(OsmsFloorUid("floor-1"), "1","Floor"),
        office = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
        description = "Init test model",
        startTime = "2024-01-01T10:00:00.0000",
        endTime = "2024-01-01T12:00:00.0000",
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateBooking(DbBookingRequest(reqUpdateSucc))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateBooking(DbBookingRequest(reqUpdateNotFound))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)

        val error = result.errors.find { it.code == "not-found" }

        assertEquals("bookingUid", error?.field)
    }

    companion object : BaseInitBookings("update") {
        override val initObjects: List<OsmsBooking> = listOf(
            createInitTestModel(OsmsBookingUid("booking-1")),
            createInitTestModel(OsmsBookingUid("booking-3")),
        )
    }
}