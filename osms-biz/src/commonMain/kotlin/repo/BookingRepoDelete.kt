package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.repo.DbBookingUidRequest
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Delete booking by UID [DB]"
    on { state == OsmsState.RUNNING }
    handle {
        val request = DbBookingUidRequest(bookingRepoPrepare)
        val result = bookingRepo.deleteBooking(request)
        if (!result.isSuccess) {
            state = OsmsState.FAILING
            errors.addAll(result.errors)
        }
        bookingRepoDone = bookingRepoRead
    }
}