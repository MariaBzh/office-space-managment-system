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
    var permissions: Set<OsmsBookingPermissions> = mutableSetOf(),
) {
    fun deepCopy(): OsmsBooking = copy(
        bookingUid = OsmsBookingUid(bookingUid.asString()),
        userUid = OsmsUserUid(userUid.asString()),
        workspaceUid = OsmsWorkspaceUid(workspaceUid.asString()),
        branch = branch.copy(
            branchUid = OsmsBranchUid(branch.branchUid.asString())
        ),
        floor = floor.copy(
            floorUid = OsmsFloorUid(floor.floorUid.asString())
        ),
        office = office.copy(
            officeUid = OsmsOfficeUid(office.officeUid.asString())
        ),
        permissions = permissions.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = OsmsBooking()
    }
}