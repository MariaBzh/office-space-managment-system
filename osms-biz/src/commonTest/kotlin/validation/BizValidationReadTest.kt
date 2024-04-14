package ru.otus.osms.biz.test.validation

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.OsmsCommand
import ru.otus.osms.repo.stubs.BookingRepoStub
import kotlin.test.Test

class BizValidationReadTest {
    private val command = OsmsCommand.READ
    private val settings by lazy {
        OsmsCorSettings(
            repoTest = BookingRepoStub()
        )
    }
    private val processor by lazy { OsmsBookingProcessor(settings) }

    @Test
    fun uidIsBlank() = validationReadUidIsBlank(command, processor)
    @Test
    fun uidIsInvalid() = validationReadUidIsNotValid(command, processor)
}
