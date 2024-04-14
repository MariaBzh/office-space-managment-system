package ru.otus.osms.biz.validation

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.errorValidation
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.common.models.OsmsBookingLock
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")

    on { bookingValidating.lock != OsmsBookingLock.NONE && !bookingValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = bookingValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
