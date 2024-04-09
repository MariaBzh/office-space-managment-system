package ru.otus.osms.biz.repo

import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.OsmsState
import ru.otus.osms.common.repo.DbBookingFilterRequest
import ru.otus.osms.cor.ICorChainDsl
import ru.otus.osms.cor.worker

fun ICorChainDsl<OsmsContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Search booking by filter [DB]"
    on { state == OsmsState.RUNNING }
    handle {
        val request = DbBookingFilterRequest(
            userUid = bookingFilterValidated.userUid,
            branch = bookingFilterValidated.branch,
            floor = bookingFilterValidated.floor,
            office = bookingFilterValidated.office,
            workspaceUid = bookingFilterValidated.workspaceUid,
            startTime = bookingFilterRequest.startTime,
            endTime = bookingFilterRequest.endTime,
        )
        val result = bookingRepo.searchBooking(request)
        val resultBookings = result.data
        if (result.isSuccess && resultBookings != null) {
            bookingsRepoDone = resultBookings.toMutableList()
        } else {
            state = OsmsState.FAILING
            errors.addAll(result.errors)
        }
    }
}