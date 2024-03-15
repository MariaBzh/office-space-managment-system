package ru.otus.osms.biz.validation

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.finishBookingValidation(title: String) = worker {
    this.title = title

    on { state == OsmsState.RUNNING }
    handle {
        bookingValidated = bookingValidating
    }
}

fun ICorChainDsl<OsmsContext>.finishBookingFilterValidation(title: String) = worker {
    this.title = title

    on { state == OsmsState.RUNNING }
    handle {
        bookingFilterValidated = bookingFilterValidating
    }
}