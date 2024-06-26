package ru.otus.osms.common

import kotlinx.datetime.Instant
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.common.stubs.OsmsStub

data class OsmsContext(
    var command: OsmsCommand = OsmsCommand.NONE,
    var state: OsmsState = OsmsState.NONE,

    val errors: MutableList<OsmsError> = mutableListOf(),
    var corSettings: OsmsCorSettings = OsmsCorSettings(),

    var workMode: OsmsWorkMode = OsmsWorkMode.TEST,
    var stubCase: OsmsStub = OsmsStub.NONE,

    var bookingRepo: IBookingRepository = IBookingRepository.NONE,
    var bookingRepoRead: OsmsBooking = OsmsBooking(),
    var bookingRepoPrepare: OsmsBooking = OsmsBooking(),
    var bookingRepoDone: OsmsBooking = OsmsBooking(),
    var bookingsRepoDone: MutableList<OsmsBooking> = mutableListOf(),

    var requestUid: OsmsRequestUid = OsmsRequestUid.NONE,
    var timeStart: Instant = Instant.NONE,
    var bookingRequest: OsmsBooking = OsmsBooking(),
    var bookingFilterRequest: OsmsBookingSearchFilter = OsmsBookingSearchFilter(),
    var bookingResponse: OsmsBooking = OsmsBooking(),

    var bookingsResponse: MutableList<OsmsBooking> = mutableListOf(),

    var bookingValidating: OsmsBooking = OsmsBooking(),
    var bookingFilterValidating: OsmsBookingSearchFilter = OsmsBookingSearchFilter(),

    var bookingValidated: OsmsBooking = OsmsBooking(),
    var bookingFilterValidated: OsmsBookingSearchFilter = OsmsBookingSearchFilter(),
)