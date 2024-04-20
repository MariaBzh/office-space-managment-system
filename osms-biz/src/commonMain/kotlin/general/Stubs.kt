package ru.otus.osms.biz.general

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.models.OsmsWorkMode
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.chain

fun ICorChainDsl<OsmsContext>.stubs(title: String, block: ICorChainDsl<OsmsContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == OsmsWorkMode.STUB && state == OsmsState.RUNNING }
}