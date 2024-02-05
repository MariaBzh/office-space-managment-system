package ru.otus.osms.stubs

import ru.otus.osms.common.models.*

object OsmsBookingStubBolts {
    val BOOKING_READ: OsmsBooking
        get() = OsmsBooking(
            bookingUid = OsmsBookingUid("booking-3000"),
            userUid = OsmsUserUid("user-1"),
            workspaceUid = OsmsWorkspaceUid("workspace-1"),
            branch = BRANCH_A,
            floor = FLOOR_1,
            office = OFFICE_MSK,
            description = "",
            startTime = "2024-01-01 10:00:00.0000",
            endTime = "2024-01-01 11:00:00.0000",
            lock = OsmsBookingLock.NONE,
            permissions = mutableSetOf(
                OsmsBookingPermissions.READ,
                OsmsBookingPermissions.UPDATE,
                OsmsBookingPermissions.DELETE,
            ),
        )

    val BOOKING_ERROR_NOT_FOUND: OsmsError
        get() = OsmsError(
            code = "not_found",
            message = "Booking not found"
        )

    val BOOKING_ERROR_BAD_UID: OsmsError
        get() = OsmsError(
            code = "bad_uid",
            group = "Validation",
            field = "'bookingUid'",
            message = "Incorrect UID"
        )

    val BOOKING_ERROR_BAD_TIME: OsmsError
        get() = OsmsError(
            code = "bad_time",
            group = "Validation",
            field = "'startTime' or 'endTime'",
            message = "Incorrect time"
        )


    val OFFICE_MSK: OsmsOffice
        get() = OsmsOffice(
            officeUid = OsmsOfficeUid("office-1"),
            name = "Московский"
        )

    val BRANCH_A: OsmsBranch
        get() = OsmsBranch(
            branchUid = OsmsBranchUid("branch-1"),
            name = "A",
        )
    val BRANCH_B: OsmsBranch
        get() = OsmsBranch(
            branchUid = OsmsBranchUid("branch-2"),
            name = "B",
        )
    val BRANCH_C: OsmsBranch
        get() = OsmsBranch(
            branchUid = OsmsBranchUid("branch-3"),
            name = "C",
        )

    val FLOOR_1: OsmsFloor
        get() = OsmsFloor(
            floorUid = OsmsFloorUid("floor-1"),
            level = "1",
        )
    val FLOOR_2: OsmsFloor
        get() = OsmsFloor(
            floorUid = OsmsFloorUid("floor-2"),
            level = "2",
        )
    val FLOOR_3: OsmsFloor
        get() = OsmsFloor(
            floorUid = OsmsFloorUid("floor-3"),
            level = "3",
        )
}