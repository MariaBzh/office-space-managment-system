package ru.otus.osms.biz.test.validation

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.OsmsCommand
import ru.otus.osms.repo.stubs.BookingRepoStub
import kotlin.test.Test

class BizValidationCreateTest {
    private val command = OsmsCommand.CREATE
    private val settings by lazy {
        OsmsCorSettings(
            repoTest = BookingRepoStub()
        )
    }
    private val processor by lazy { OsmsBookingProcessor(settings) }

    @Test
    fun uidIsBlank() = validationCreateUidIsBlank(command, processor)
    @Test
    fun uidIsInvalid() = validationCreateUidIsNotValid(command, processor)
    @Test
    fun timeIsBlankAndInvalidFormat() = validationBlankTimeAndFormatTime(command, processor)
    @Test
    fun timeRangeIsInvalid() = validationTimeIsInvalid(command, processor)
}
