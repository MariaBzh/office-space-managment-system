package ru.otus.osms.common

import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.logging.common.OsmsLoggerProvider

class OsmsCorSettings(
    val loggerProvider: OsmsLoggerProvider = OsmsLoggerProvider(),
    val repoStub: IBookingRepository = IBookingRepository.NONE,
    val repoTest: IBookingRepository = IBookingRepository.NONE,
    val repoProd: IBookingRepository = IBookingRepository.NONE,
) {
    companion object {
        val NONE = OsmsCorSettings()
    }
}