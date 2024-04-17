package ru.otus.osms.biz.validation

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.chain

fun ICorChainDsl<OsmsContext>.validation(block: ICorChainDsl<OsmsContext>.() -> Unit) = chain {
    block()
    title = "Validation"

    on { state == OsmsState.RUNNING }
}