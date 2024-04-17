package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsBookingSearchFilter
import ru.otus.osms.common.models.OsmsCommand
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.models.OsmsWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest {
    private val command = OsmsCommand.SEARCH
    private val processor by lazy { OsmsBookingProcessor() }

    @Test
    fun correctEmpty() = runTest {
        val ctx = OsmsContext(
            command = command,
            state = OsmsState.NONE,
            workMode = OsmsWorkMode.TEST,
            bookingFilterRequest = OsmsBookingSearchFilter()
        )
        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(OsmsState.FAILING, ctx.state)
    }
}
