package ru.otus.osms.biz.groups

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsCommand
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.chain

fun ICorChainDsl<OsmsContext>.operation(
    title: String,
    command: OsmsCommand,
    block: ICorChainDsl<OsmsContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on {
        this.command == command && state == OsmsState.RUNNING
    }
}
