package ru.otus.osms.repo.pg.test

import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.repo.test.*
import kotlin.random.Random

val random = Random(System.currentTimeMillis())
class RepoBookingSQLCreateTest : RepoBookingCreateTest() {
    override val repo: IBookingRepository = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        randomUuid = { lockNew.asString() },
    )
}

class RepoBookingSQLDeleteTest : RepoBookingDeleteTest() {
    override val repo: IBookingRepository = SqlTestCompanion.repoUnderTestContainer(
        "delete-" + random.nextInt(),
    )
}

class RepoBookingSQLReadTest : RepoBookingReadTest() {
    override val repo: IBookingRepository = SqlTestCompanion.repoUnderTestContainer(
        "read-" + random.nextInt(),
    )
}

class RepoBookingSQLSearchTest : RepoBookingSearchTest() {
    override val repo: IBookingRepository = SqlTestCompanion.repoUnderTestContainer(
        "search-" + random.nextInt(),
    )
}

class RepoBookingSQLUpdateTest : RepoBookingUpdateTest() {
    override val repo: IBookingRepository = SqlTestCompanion.repoUnderTestContainer(
        "update-" + random.nextInt(),
        randomUuid = { lockNew.asString() },
    )
}
