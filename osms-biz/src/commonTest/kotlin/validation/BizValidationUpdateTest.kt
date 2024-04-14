package ru.otus.osms.biz.test.validation

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.OsmsCommand
import ru.otus.osms.repo.stubs.BookingRepoStub
import kotlin.test.Test

class BizValidationUpdateTest {
    private val command = OsmsCommand.UPDATE
    private val settings by lazy {
        OsmsCorSettings(
            repoTest = BookingRepoStub()
        )
    }
    private val processor by lazy { OsmsBookingProcessor(settings) }

    @Test
    fun uidIsBlank() = validationUpdateUidIsBlank(command, processor)
    @Test
    fun uidIsInvalid() = validationUpdateUidIsNotValid(command, processor)
    @Test
    fun timeIsBlankAndInvalidFormat() = validationBlankTimeAndFormatTime(command, processor)
    @Test
    fun timeRangeIsInvalid() = validationTimeIsInvalid(command, processor)
    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)
}
