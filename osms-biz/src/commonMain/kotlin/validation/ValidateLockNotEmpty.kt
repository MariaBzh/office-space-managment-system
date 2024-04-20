package ru.otus.osms.biz.validation

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.errorValidation
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { bookingValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
