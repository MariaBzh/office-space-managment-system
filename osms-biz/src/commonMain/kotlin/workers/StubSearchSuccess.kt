package ru.otus.osms.biz.workers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker
import ru.otus.osms.stubs.OsmsBookingStub

fun ICorChainDsl<OsmsContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == OsmsStub.SUCCESS && state == OsmsState.RUNNING }
    handle {
        state = OsmsState.FINISHING
        bookingsResponse
            .addAll(
                OsmsBookingStub.prepareSearchList()
            )
    }
}
