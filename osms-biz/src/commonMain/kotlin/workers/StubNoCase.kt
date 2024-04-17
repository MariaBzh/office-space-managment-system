package ru.otus.osms.biz.workers

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.helpers.fail
import ru.otus.osms.common.models.OsmsError
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == OsmsState.RUNNING }
    handle {
        fail(
            OsmsError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
