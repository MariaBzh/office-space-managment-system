package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.repo.DbBookingRequest
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoUpdate(title: String) = worker {
    this.title = title
    description = "Update booking [DB]"
    on { state == OsmsState.RUNNING }
    handle {
        val request = DbBookingRequest(bookingRepoPrepare)
        val result = bookingRepo.updateBooking(request)
        val resultBooking = result.data
        if (result.isSuccess && resultBooking != null) {
            bookingRepoDone = resultBooking
        } else {
            state = OsmsState.FAILING
            errors.addAll(result.errors)
            bookingRepoDone
        }
    }
}
