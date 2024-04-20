package ru.otus.osms.db.inmemory.test

import ru.otus.osms.db.inmemory.BookingRepoInMemory
import ru.otus.osms.repo.test.RepoBookingCreateTest

class BookingRepoInMemoryCreateTest : RepoBookingCreateTest() {
    override val repo = BookingRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
