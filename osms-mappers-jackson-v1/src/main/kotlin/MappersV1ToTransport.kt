package ru.otus.osms.mappers.jackson.v1

import ru.otus.osms.api.v1.models.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.mappers.jackson.v1.exceptions.UnknownOsmsCommand

fun OsmsContext.toTransportBooking(): IResponse = when (val command = command) {
    OsmsCommand.CREATE -> toTransportCreate()
    OsmsCommand.READ -> toTransportRead()
    OsmsCommand.UPDATE -> toTransportUpdate()
    OsmsCommand.DELETE -> toTransportDelete()
    OsmsCommand.SEARCH -> toTransportSearch()
    OsmsCommand.NONE -> throw UnknownOsmsCommand(command)
}

fun OsmsContext.toTransportCreate() = BookingCreateResponse(
    requestUid = this.requestUid.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    booking = bookingResponse.toTransportBooking()
)

fun OsmsContext.toTransportRead() = BookingReadResponse(
    requestUid = this.requestUid.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    booking = bookingResponse.toTransportBooking()
)

fun OsmsContext.toTransportUpdate() = BookingUpdateResponse(
    requestUid = this.requestUid.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    booking = bookingResponse.toTransportBooking()
)

fun OsmsContext.toTransportDelete() = BookingDeleteResponse(
    requestUid = this.requestUid.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    booking = bookingResponse.toTransportBooking()
)

fun OsmsContext.toTransportSearch() = BookingSearchResponse(
    requestUid = this.requestUid.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    bookings = bookingsResponse.toTransportBooking()
)

fun List<OsmsBooking>.toTransportBooking(): List<BookingResponseObject>? = this
    .map { it.toTransportBooking() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun OsmsContext.toTransportInit() = BookingInitResponse(
    requestUid = this.requestUid.asString().takeIf { it.isNotBlank() },
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

private fun OsmsBooking.toTransportBooking(): BookingResponseObject = BookingResponseObject(
    bookingUid = bookingUid.takeIf { it != OsmsBookingUid.NONE }?.asString(),
    userUid = userUid.takeIf { it != OsmsUserUid.NONE }?.asString(),
    branch = branch.toTransportBranch(),
    floor = floor.toTransportFloor(),
    office = office.toTransportOffice(),
    workspaceUid = workspaceUid.takeIf { it != OsmsWorkspaceUid.NONE }?.asString(),
    description = description.takeIf { it.isNotBlank() },
    startTime = startTime.takeIf { it.isNotBlank() },
    endTime = endTime.takeIf { it.isNotBlank() },
    permissions = permissions.toTransportBooking(),
    lock = lock.takeIf { it != OsmsBookingLock.NONE }?.asString(),
)

private fun Set<OsmsBookingPermissions>.toTransportBooking(): Set<BookingPermissions>? = this
    .map { it.toTransportBooking() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun OsmsBookingPermissions.toTransportBooking() = when (this) {
    OsmsBookingPermissions.READ -> BookingPermissions.READ
    OsmsBookingPermissions.UPDATE -> BookingPermissions.UPDATE
    OsmsBookingPermissions.DELETE -> BookingPermissions.DELETE
}

private fun List<OsmsError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportBooking() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun OsmsError.toTransportBooking() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun OsmsBranch.toTransportBranch() = Branch(
    branchUid = this.branchUid.asString().takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)

private fun OsmsFloor.toTransportFloor() = Floor(
    floorUid = this.floorUid.asString().takeIf { it.isNotBlank() },
    level = level.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)

private fun OsmsOffice.toTransportOffice() = Office(
    officeUid = this.officeUid.asString().takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)

private fun OsmsState.toResult(): ResponseResult? = when (this) {
    OsmsState.RUNNING, OsmsState.FINISHING -> ResponseResult.SUCCESS
    OsmsState.FAILING -> ResponseResult.ERROR
    OsmsState.NONE -> null
}
