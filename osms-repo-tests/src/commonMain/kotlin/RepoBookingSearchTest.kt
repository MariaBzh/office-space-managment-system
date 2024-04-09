package ru.otus.osms.repo.test

import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsBookingUid
import ru.otus.osms.common.models.OsmsUserUid
import ru.otus.osms.common.models.OsmsWorkspaceUid
import ru.otus.osms.common.repo.DbBookingFilterRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingSearchTest {
    abstract val repo: IBookingRepository

    protected open val initializedObjects: List<OsmsBooking> = initObjects

    @Test
    fun searchBooking() = runRepoTest {
        val result = repo.searchBooking(
            DbBookingFilterRequest(userUid = searchOwnerUid)
        )

        assertEquals(true, result.isSuccess)

        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.bookingUid.asString() }

        assertEquals(expected, result.data?.sortedBy { it.bookingUid.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitBookings("search") {
        private val searchOwnerUid = OsmsUserUid("user-1")
        private val searchWorkspaceUid = OsmsWorkspaceUid("workplace-1")
        private val searchTimeRange = Pair("2024-01-01T10:00:00", "2024-01-01T12:00:00")

        override val initObjects: List<OsmsBooking> = listOf(
            createInitTestModel(OsmsBookingUid("booking-1")),
            createInitTestModel(OsmsBookingUid("booking-2"), userUid = searchOwnerUid),
            createInitTestModel(OsmsBookingUid("booking-3"), workspaceUid = searchWorkspaceUid),
            createInitTestModel(OsmsBookingUid("booking-4"), startTime = searchTimeRange.first, endTime = searchTimeRange.second),
        )
    }
}