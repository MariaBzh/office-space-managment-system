package ru.otus.osms.biz.test.validation

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.models.OsmsCommand
import kotlin.test.Test

class BizValidationUpdateTest {
    private val command = OsmsCommand.UPDATE
    private val processor by lazy { OsmsBookingProcessor() }

    @Test
    fun uidIsBlank() = validationUpdateUidIsBlank(command, processor)
    @Test
    fun uidIsInvalid() = validationUpdateUidIsNotValid(command, processor)
    @Test
    fun timeIsBlankAndInvalidFormat() = validationBlankTimeAndFormatTime(command, processor)
    @Test
    fun timeRangeIsInvalid() = validationTimeIsInvalid(command, processor)
}
