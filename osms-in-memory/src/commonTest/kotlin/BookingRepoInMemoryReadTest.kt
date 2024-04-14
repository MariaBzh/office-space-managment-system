package ru.otus.osms.db.inmemory.test

import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.db.inmemory.BookingRepoInMemory
import ru.otus.osms.repo.test.RepoBookingReadTest

class BookingRepoInMemoryReadTest: RepoBookingReadTest() {
    override val repo: IBookingRepository = BookingRepoInMemory(
        initObjects = initObjects
    )
}
