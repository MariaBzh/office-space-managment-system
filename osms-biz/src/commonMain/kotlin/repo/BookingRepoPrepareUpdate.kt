package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Prepare booking to update in DB"
    on { state == OsmsState.RUNNING }
    handle {
        bookingRepoPrepare = bookingRepoRead.deepCopy().apply {
            bookingUid = bookingValidated.bookingUid
            userUid = bookingValidated.userUid
            workspaceUid = bookingValidated.workspaceUid
            branch = bookingValidated.branch
            floor = bookingValidated.floor
            office = bookingValidated.office
            description = bookingValidated.description
            startTime = bookingValidated.startTime
            endTime = bookingValidated.endTime
            lock = bookingValidated.lock
            permissions = bookingValidated.permissions
        }
    }
}