package ru.otus.osms.biz.workers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker
import ru.otus.osms.stubs.OsmsBookingStub

fun ICorChainDsl<OsmsContext>.stubValidationBadTime(title: String) = worker {
    this.title = title
    on { stubCase == OsmsStub.BAD_TIME && state == OsmsState.RUNNING }
    handle {
        state = OsmsState.FAILING
        this.errors.add(
            OsmsBookingStub.getError(OsmsStub.BAD_TIME.name)
        )
    }
}
