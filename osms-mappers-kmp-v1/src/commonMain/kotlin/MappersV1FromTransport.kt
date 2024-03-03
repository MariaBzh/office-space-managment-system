package ru.otus.osms.mappers.kmp.v1

import ru.otus.osms.api.v1.kpm.models.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.mappers.kmp.v1.exceptions.UnknownRequestClass

fun OsmsContext.fromTransport(request: IRequest) = when (request) {
    is BookingCreateRequest -> fromTransport(request)
    is BookingReadRequest -> fromTransport(request)
    is BookingUpdateRequest -> fromTransport(request)
    is BookingDeleteRequest -> fromTransport(request)
    is BookingSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

private fun String?.toBookingUid() = this?.let { OsmsBookingUid(it) } ?: OsmsBookingUid.NONE
private fun String?.toBookingWithUid() = OsmsBooking(bookingUid = this.toBookingUid())
private fun String?.toUserUid() = this?.let { OsmsUserUid(it) } ?: OsmsUserUid.NONE
private fun String?.toBranchUid() = this?.let { OsmsBranchUid(it) } ?: OsmsBranchUid.NONE
private fun String?.toFloorUid() = this?.let { OsmsFloorUid(it) } ?: OsmsFloorUid.NONE
private fun String?.toOfficeUid() = this?.let { OsmsOfficeUid(it) } ?: OsmsOfficeUid.NONE
private fun String?.toWorkspaceUid() = this?.let { OsmsWorkspaceUid(it) } ?: OsmsWorkspaceUid.NONE
private fun IRequest?.requestUid() = this?.requestUid?.let { OsmsRequestUid(it) } ?: OsmsRequestUid.NONE

private fun BookingDebug?.transportToWorkMode(): OsmsWorkMode = when (this?.mode) {
    BookingRequestDebugMode.PROD -> OsmsWorkMode.PROD
    BookingRequestDebugMode.TEST -> OsmsWorkMode.TEST
    BookingRequestDebugMode.STUB -> OsmsWorkMode.STUB
    null -> OsmsWorkMode.TEST
}

private fun BookingDebug?.transportToStubCase(): OsmsStub = when (this?.stub) {
    BookingRequestDebugStubs.SUCCESS -> OsmsStub.SUCCESS
    BookingRequestDebugStubs.NOT_FOUND -> OsmsStub.NOT_FOUND
    BookingRequestDebugStubs.BAD_UID -> OsmsStub.BAD_UID
    BookingRequestDebugStubs.BAD_TIME -> OsmsStub.BAD_TIME
    null -> OsmsStub.NONE
}

fun OsmsContext.fromTransport(request: BookingCreateRequest) {
    command = OsmsCommand.CREATE
    requestUid = request.requestUid()
    bookingRequest = request.booking?.toInternal() ?: OsmsBooking()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OsmsContext.fromTransport(request: BookingReadRequest) {
    command = OsmsCommand.READ
    requestUid = request.requestUid()
    bookingRequest = request.booking?.bookingUid.toBookingWithUid()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OsmsContext.fromTransport(request: BookingUpdateRequest) {
    command = OsmsCommand.UPDATE
    requestUid = request.requestUid()
    bookingRequest = request.booking?.toInternal() ?: OsmsBooking()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OsmsContext.fromTransport(request: BookingDeleteRequest) {
    command = OsmsCommand.DELETE
    requestUid = request.requestUid()
    bookingRequest = request.booking?.bookingUid.toBookingWithUid()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OsmsContext.fromTransport(request: BookingSearchRequest) {
    command = OsmsCommand.SEARCH
    requestUid = request.requestUid()
    bookingFilterRequest = request.bookingFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun BookingSearchFilter?.toInternal(): OsmsBookingSearchFilter = OsmsBookingSearchFilter(
    userUid = this?.userUid.toUserUid(),
    branch = this?.branch.toInternal(),
    floor = this?.floor.toInternal(),
    office = this?.office.toInternal(),
    workspaceUid = this?.workspaceUid.toWorkspaceUid(),
    startTime = this?.startTime ?: "",
    endTime = this?.endTime ?: "",
)

private fun BookingCreateObject.toInternal(): OsmsBooking = OsmsBooking(
    userUid = this.userUid.toUserUid(),
    workspaceUid = this.workspaceUid.toWorkspaceUid(),
    branch = this.branch.toInternal(),
    floor = this.floor.toInternal(),
    office = this.office.toInternal(),
    description = this.description ?: "",
    startTime = this.startTime ?: "",
    endTime = this.endTime ?: "",
    permissions = mutableSetOf(),
)

private fun BookingUpdateObject.toInternal(): OsmsBooking = OsmsBooking(
    bookingUid = this.bookingUid.toBookingUid(),
    userUid = this.userUid.toUserUid(),
    workspaceUid = this.workspaceUid.toWorkspaceUid(),
    branch = this.branch.toInternal(),
    floor = this.floor.toInternal(),
    office = this.office.toInternal(),
    description = this.description ?: "",
    startTime = this.startTime ?: "",
    endTime = this.endTime ?: "",
    permissions = mutableSetOf(),
)

private fun Branch?.toInternal(): OsmsBranch =
    this?.let {
        OsmsBranch(
            branchUid = this.branchUid.toBranchUid(),
            name = this.name ?: "",
            description = this.description ?: "",
        )
    } ?: OsmsBranch.NONE

private fun Floor?.toInternal(): OsmsFloor =
    this?.let {
        OsmsFloor(
            floorUid = this.floorUid.toFloorUid(),
            level = this.level ?: "",
            description = this.description ?: "",
        )
    } ?: OsmsFloor.NONE

private fun Office?.toInternal(): OsmsOffice =
    this?.let {
        OsmsOffice(
            officeUid = this.officeUid.toOfficeUid(),
            name = this.name ?: "",
            description = this.description ?: "",
        )
    } ?: OsmsOffice.NONE
