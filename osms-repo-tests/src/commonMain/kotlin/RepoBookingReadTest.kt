package ru.otus.osms.repo.test

import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsBookingUid
import ru.otus.osms.common.repo.DbBookingUidRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingReadTest {
    abstract val repo: IBookingRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readBooking(DbBookingUidRequest(readSucc.bookingUid))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readBooking(DbBookingUidRequest(NOT_FOUND_UID))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)

        val error = result.errors.find { it.code == "not-found" }

        assertEquals("bookingUid", error?.field)
    }

    companion object : BaseInitBookings() {
        override val initObjects: List<OsmsBooking> = listOf(
            createInitTestModel(OsmsBookingUid("booking-1"))
        )

        val NOT_FOUND_UID = OsmsBookingUid("booking-1-not-found")
    }
}