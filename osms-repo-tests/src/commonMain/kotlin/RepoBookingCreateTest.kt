package ru.otus.osms.repo.test

import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingCreateTest {
    abstract val repo: IBookingRepository

    private val createObj = OsmsBooking(
        userUid = OsmsUserUid("user-1"),
        workspaceUid = OsmsWorkspaceUid("workspace-1"),
        branch = OsmsBranch(OsmsBranchUid("branch-1"), "Branch"),
        floor = OsmsFloor(OsmsFloorUid("floor-1"), "1", "Floor"),
        office = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
        description = "Test",
        startTime = "2024-01-01T10:00:00",
        endTime = "2024-01-01T12:00:00",
        permissions = setOf(OsmsBookingPermissions.READ),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createBooking(DbBookingRequest(createObj))
        val expected = createObj.copy(bookingUid = result.data?.bookingUid ?: OsmsBookingUid.NONE)

        assertEquals(expected.bookingUid, result.data?.bookingUid)
        assertEquals(expected.userUid, result.data?.userUid)
        assertEquals(expected.branch, result.data?.branch)
        assertEquals(expected.floor, result.data?.floor)
        assertEquals(expected.office, result.data?.office)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.startTime, result.data?.startTime)
        assertEquals(expected.endTime, result.data?.endTime)
        assertEquals(expected.permissions, result.data?.permissions)

        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitBookings("create") {
        override val initObjects: List<OsmsBooking> = emptyList()
    }
}