package ru.otus.osms.common.models

data class OsmsBooking(
    var bookingUid: OsmsBookingUid = OsmsBookingUid.NONE,
    var userUid: OsmsUserUid = OsmsUserUid.NONE,
    var workspaceUid: OsmsWorkspaceUid = OsmsWorkspaceUid.NONE,
    var branch: OsmsBranch = OsmsBranch.NONE,
    var floor: OsmsFloor = OsmsFloor.NONE,
    var office: OsmsOffice = OsmsOffice.NONE,
    var description: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var lock: OsmsBookingLock = OsmsBookingLock.NONE,
    val permissions: Set<OsmsBookingPermissions> = mutableSetOf(),
)