package ru.otus.osms.db.inmemory.models

import models.PermissionEntity
import ru.otus.osms.common.models.*

data class BookingEntity (
    val bookingUid: String? = null,
    val userUid: String? = null,
    val workplaceUid: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val description: String? = null,
    val branch: BranchEntity? = null,
    val floor: FloorEntity? = null,
    val office: OfficeEntity? = null,
    val permissions: Set<PermissionEntity>? = null,
    val lock: String? = null,
) {
    constructor(booking: OsmsBooking): this(
        booking.bookingUid.asString(),
        booking.userUid.asString(),
        booking.workspaceUid.asString(),
        booking.startTime,
        booking.endTime,
        booking.description,
        BranchEntity(
            booking.branch.branchUid.asString(),
            booking.branch.name,
            booking.branch.description,
        ),
        FloorEntity(
            booking.floor.floorUid.asString(),
            booking.floor.level,
            booking.floor.description,
        ),
        OfficeEntity(
            booking.office.officeUid.asString(),
            booking.office.name,
            booking.office.description,
        ),
        booking.permissions
            .map { permissions -> PermissionEntity(permissions.name) }
            .toSet(),
        booking.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = OsmsBooking(
        bookingUid = bookingUid?.let { OsmsBookingUid(it) } ?: OsmsBookingUid.NONE,
        userUid = userUid?.let { OsmsUserUid(it) } ?: OsmsUserUid.NONE,
        workspaceUid = workplaceUid?.let { OsmsWorkspaceUid(it) } ?: OsmsWorkspaceUid.NONE,
        startTime = startTime ?: "",
        endTime = endTime ?: "",
        description = description ?: "",
        branch = branch?.toInternal() ?: OsmsBranch.NONE,
        floor = floor?.toInternal() ?: OsmsFloor.NONE,
        office = office?.toInternal() ?: OsmsOffice.NONE,
        permissions = permissions?.map { it.toInternal() }?.toSet() ?: setOf(OsmsBookingPermissions.READ),
        lock = lock?.let { OsmsBookingLock(it) } ?: OsmsBookingLock.NONE,
    )
}
