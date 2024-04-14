package ru.otus.osms.repo.test

import ru.otus.osms.common.models.*

abstract class BaseInitBookings : IInitObjects<OsmsBooking> {
    open val lockOld: OsmsBookingLock = OsmsBookingLock("lock-1")
    open val lockBad: OsmsBookingLock = OsmsBookingLock("lock-2")

    fun createInitTestModel(
        bookingUid: OsmsBookingUid = OsmsBookingUid("booking-1"),
        userUid: OsmsUserUid = OsmsUserUid("user-1"),
        workspaceUid: OsmsWorkspaceUid = OsmsWorkspaceUid("workspace-1"),
        branch: OsmsBranch = OsmsBranch(OsmsBranchUid("branch-1"),"Branch", "Test"),
        floor: OsmsFloor = OsmsFloor(OsmsFloorUid("floor-1"), "1A","Test"),
        office: OsmsOffice = OsmsOffice(OsmsOfficeUid("office-1"), "Office", "Test"),
        description: String = "",
        startTime: String = "2024-01-01T10:00:00",
        endTime: String = "2024-01-01T12:00:00",
        lock: OsmsBookingLock = lockOld,
        permissions: Set<OsmsBookingPermissions>? = null,
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
        permissions = permissions ?: setOf(OsmsBookingPermissions.READ, OsmsBookingPermissions.UPDATE, OsmsBookingPermissions.DELETE),
        lock = lock,
    )
}
