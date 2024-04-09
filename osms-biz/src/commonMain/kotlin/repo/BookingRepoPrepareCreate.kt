package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Prepare booking to save in DB"
    on { state == OsmsState.RUNNING }
    handle {
        bookingRepoRead = bookingValidated.deepCopy()
        bookingRepoPrepare = bookingRepoRead

    }
}