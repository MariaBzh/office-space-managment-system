package ru.otus.osms.repo.test

import ru.otus.osms.common.models.*

abstract class BaseInitBookings(op: String): IInitObjects<OsmsBooking> {

    fun createInitTestModel(
        bookingUid: OsmsBookingUid = OsmsBookingUid("booking-1"),
        userUid: OsmsUserUid = OsmsUserUid("user-1"),
        workspaceUid: OsmsWorkspaceUid = OsmsWorkspaceUid("workspace-1"),
        branch: OsmsBranch = OsmsBranch(OsmsBranchUid("branch-1"),"Branch"),
        floor: OsmsFloor = OsmsFloor(OsmsFloorUid("floor-1"), "1","Floor"),
        office: OsmsOffice = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
        description: String = "Init test model",
        startTime: String = "2024-01-01T10:00:00.0000",
        endTime: String = "2024-01-01T12:00:00.0000",
    ) = OsmsBooking(
        bookingUid = bookingUid,
        userUid = userUid,
        workspaceUid = workspaceUid,
        branch = branch,
        floor = floor,
        office = office,
        description = description,
        startTime = startTime,
        endTime = endTime,
        permissions = setOf(OsmsBookingPermissions.READ, OsmsBookingPermissions.UPDATE, OsmsBookingPermissions.DELETE)
    )
}
