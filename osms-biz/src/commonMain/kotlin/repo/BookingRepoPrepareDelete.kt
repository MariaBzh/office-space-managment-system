package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Prepare booking to delete from DB"
    on { state == OsmsState.RUNNING }
    handle {
        bookingRepoPrepare = bookingValidated.deepCopy()
    }
}