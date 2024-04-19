package ru.otus.osms.repo.test

import kotlinx.coroutines.test.runTest
import ru.otus.osms.common.models.OsmsBooking
import ru.otus.osms.common.models.OsmsBookingUid
import ru.otus.osms.common.repo.DbBookingUidRequest
import ru.otus.osms.common.repo.IBookingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBookingDeleteTest {
    abstract val repo: IBookingRepository

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteBooking(DbBookingUidRequest(successId, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteConcurrency() = runTest {
        val result = repo.deleteBooking(DbBookingUidRequest(concurrencyId, lock = lockBad))

        assertEquals(false, result.isSuccess)

        val error = result.errors.find { it.code == "concurrency" }
        
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readBooking(DbBookingUidRequest(NOT_FOUND_UID, lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)

        val error = result.errors.find { it.code == "not-found" }

        assertEquals("bookingUid", error?.field)
    }

    companion object : BaseInitBookings() {
        override val initObjects: List<OsmsBooking> = listOf(
            createInitTestModel(OsmsBookingUid("booking-1")),
            createInitTestModel(OsmsBookingUid("booking-2")),
        )
        val NOT_FOUND_UID = OsmsBookingUid("booking-1-not-found")
        val successId = OsmsBookingUid(initObjects[0].bookingUid.asString())
        val concurrencyId = OsmsBookingUid(initObjects[1].bookingUid.asString())
    }
}