package ru.otus.osms.biz.test.validation

import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.models.OsmsCommand
import kotlin.test.Test

class BizValidationDeleteTest {
    private val command = OsmsCommand.DELETE
    private val processor by lazy { OsmsBookingProcessor() }

    @Test
    fun uidIsBlank() = validationReadUidIsBlank(command, processor)
    @Test
    fun uidIsInvalid() = validationReadUidIsNotValid(command, processor)
}
