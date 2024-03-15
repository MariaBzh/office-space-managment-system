package ru.otus.osms.biz.workers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.initStatus(title: String) = worker {
    this.title = title
    on {
        state == OsmsState.NONE
    }
    handle {
        state = OsmsState.RUNNING
    }
}
