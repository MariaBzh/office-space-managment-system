package ru.otus.osms.biz.general

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.models.OsmsWorkMode
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != OsmsWorkMode.STUB }
    handle {
        bookingResponse = bookingRepoDone
        bookingsResponse = bookingsRepoDone
        state = when (val state = state) {
            OsmsState.RUNNING -> OsmsState.FINISHING
            else -> state
        }
    }
}
