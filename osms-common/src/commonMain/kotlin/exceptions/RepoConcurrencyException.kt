package ru.otus.osms.common.exceptions

import ru.otus.osms.common.models.OsmsBookingLock

class RepoConcurrencyException(expectedLock: OsmsBookingLock, actualLock: OsmsBookingLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
