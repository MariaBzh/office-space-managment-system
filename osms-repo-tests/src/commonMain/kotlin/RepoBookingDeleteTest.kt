package ru.otus.osms.repo.test

import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsBookingUid
import ru.otus.osms.common.repo.DbBookingUidRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingDeleteTest {
    abstract val repo: IBookingRepository
    protected open val deleteSucc = initObjects[0]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteBooking(DbBookingUidRequest(deleteSucc.bookingUid))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readBooking(DbBookingUidRequest(NOT_FOUND_UID))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)

        val error = result.errors.find { it.code == "not-found" }

        assertEquals("bookingUid", error?.field)
    }

    companion object : BaseInitBookings("delete") {
        override val initObjects: List<OsmsBooking> = listOf(
            createInitTestModel(OsmsBookingUid("delete")),
            createInitTestModel(OsmsBookingUid("deleteLock")),
        )
        val NOT_FOUND_UID = OsmsBookingUid("booking-1-not-found")
    }
}