package ru.otus.osms.db.test

import ru.otus.osms.db.BookingRepoInMemory
import ru.otus.osms.repo.test.RepoBookingCreateTest

class BookingRepoInMemoryCreateTest : RepoBookingCreateTest() {
    override val repo = BookingRepoInMemory(
        initObjects = initObjects,
    )
}
