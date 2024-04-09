package ru.otus.osms.db.test

import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.db.BookingRepoInMemory
import ru.otus.osms.repo.test.RepoBookingSearchTest

class BookingRepoInMemorySearchTest : RepoBookingSearchTest() {
    override val repo: IBookingRepository = BookingRepoInMemory(
        initObjects = initObjects
    )
}
