import kotlinx.datetime.Clock
import ru.otus.osms.api.logs.models.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*

fun OsmsContext.toLog(logUid: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logUid = logUid,
    source = "OSMS",
    booking = toOsmsLog(),
    errors = errors.map { it.toLog() }
)

fun OsmsContext.toOsmsLog(): OsmsLogModel? {
    val bookingNone = OsmsBooking()

    return OsmsLogModel(
        requestUid = requestUid.takeIf { it != OsmsRequestUid.NONE }?.asString(),
        requestBooking = bookingRequest.takeIf { it != bookingNone }?.toLog(),
        responseBooking = bookingResponse.takeIf { it != bookingNone }?.toLog(),
        responseBookings = bookingsResponse.takeIf { it.isNotEmpty() }?.filter { it != bookingNone }?.map { it.toLog() },
        requestFilter = bookingFilterRequest.takeIf { it != OsmsBookingSearchFilter() }?.toLog(bookingRequest.bookingUid),
    ).takeIf { it != OsmsLogModel() }
}

private fun OsmsBookingSearchFilter.toLog(bookingUid: OsmsBookingUid) = BookingFilterLog(
    bookingUid = bookingUid.takeIf { it != OsmsBookingUid.NONE }?.asString(),
    userUid = userUid.takeIf { it != OsmsUserUid.NONE }?.asString(),
    branchUid = branch.branchUid.takeIf { it != OsmsBranchUid.NONE }?.asString(),
    floorUid = floor.floorUid.takeIf { it != OsmsFloorUid.NONE }?.asString(),
    officeUid = office.officeUid.takeIf { it != OsmsOfficeUid.NONE }?.asString(),
    workspaceUid = workspaceUid.takeIf { it != OsmsWorkspaceUid.NONE }?.asString(),
    startTime = startTime.takeIf { it.isNotBlank() },
    endTime = endTime.takeIf { it.isNotBlank() },
)

fun OsmsError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun OsmsBooking.toLog() = BookingLog(
    bookingUid = bookingUid.takeIf { it != OsmsBookingUid.NONE }?.asString(),
    userUid = userUid.takeIf { it != OsmsUserUid.NONE }?.asString(),
    branchUid = branch.branchUid.takeIf { it != OsmsBranchUid.NONE }?.asString(),
    floorUid = floor.floorUid.takeIf { it != OsmsFloorUid.NONE }?.asString(),
    officeUid = office.officeUid.takeIf { it != OsmsOfficeUid.NONE }?.asString(),
    workspaceUid = workspaceUid.takeIf { it != OsmsWorkspaceUid.NONE }?.asString(),
    startTime = startTime.takeIf { it.isNotBlank() },
    endTime = endTime.takeIf { it.isNotBlank() },
)
