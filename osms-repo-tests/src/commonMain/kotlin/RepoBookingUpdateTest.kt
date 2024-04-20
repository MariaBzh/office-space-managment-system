package ru.otus.osms.repo.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingUpdateTest {
    abstract val repo: IBookingRepository
    protected open val updateSuccess = initObjects[0]
    protected val updateConc = initObjects[1]
    private val updateUidNotFound = OsmsBookingUid("booking-3")
    protected val lockBad = OsmsBookingLock("lock-2")
    protected val lockNew = OsmsBookingLock("lock-3")

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
            lock = initObjects.first().lock,
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
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc = OsmsBooking(
        bookingUid = updateConc.bookingUid,
        userUid = OsmsUserUid("user-1"),
        workspaceUid = OsmsWorkspaceUid("workspace-1"),
        branch = OsmsBranch(OsmsBranchUid("branch-1"),"Branch"),
        floor = OsmsFloor(OsmsFloorUid("floor-1"), "1","Floor"),
        office = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
        description = "Init test model",
        startTime = "2024-01-01T10:00:00.0000",
        endTime = "2024-01-01T12:00:00.0000",
        lock = lockBad,
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

    @Test
    fun updateConcurrencyError() = runTest {
        val result = repo.updateBooking(DbBookingRequest(reqUpdateConc))

        assertEquals(false, result.isSuccess)

        val error = result.errors.find { it.code == "concurrency" }

        assertEquals("lock", error?.field)

        assertEquals(updateConc.bookingUid, result.data?.bookingUid)
        assertEquals(updateConc.userUid, result.data?.userUid)
        assertEquals(updateConc.branch, result.data?.branch)
        assertEquals(updateConc.floor, result.data?.floor)
        assertEquals(updateConc.office, result.data?.office)
        assertEquals(updateConc.description, result.data?.description)
        assertEquals(updateConc.startTime, result.data?.startTime)
        assertEquals(updateConc.endTime, result.data?.endTime)


    }

    companion object : BaseInitBookings() {
        override val initObjects: List<OsmsBooking> = listOf(
            createInitTestModel(OsmsBookingUid("booking-1")),
            createInitTestModel(OsmsBookingUid("booking-2")),
        )
    }
}