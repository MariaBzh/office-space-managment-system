package ru.otus.osms.biz.test.validation

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.models.OsmsCommand
import kotlin.test.Test

class BizValidationCreateTest {
    private val command = OsmsCommand.CREATE
    private val processor by lazy { OsmsBookingProcessor() }

    @Test
    fun uidIsBlank() = validationCreateUidIsBlank(command, processor)
    @Test
    fun uidIsInvalid() = validationCreateUidIsNotValid(command, processor)
    @Test
    fun timeIsBlankAndInvalidFormat() = validationBlankTimeAndFormatTime(command, processor)
    @Test
    fun timeRangeIsInvalid() = validationTimeIsInvalid(command, processor)
}
