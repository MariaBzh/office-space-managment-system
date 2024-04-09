package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.repo.DbBookingUidRequest
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Read booking [DB]"
    on { state == OsmsState.RUNNING }
    handle {
        val request = DbBookingUidRequest(bookingValidated)
        val result = bookingRepo.readBooking(request)
        val resultBooking = result.data
        if (result.isSuccess && resultBooking != null) {
            bookingRepoRead = resultBooking
        } else {
            state = OsmsState.FAILING
            errors.addAll(result.errors)
        }
    }
}