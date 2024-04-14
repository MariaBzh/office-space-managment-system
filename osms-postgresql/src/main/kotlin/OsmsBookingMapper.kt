package ru.otus.osms.repo.pg

import org.jooq.Record
import org.jooq.Result
import ru.otus.osms.common.models.*
import ru.otus.osms.repo.tables.*
import java.time.format.DateTimeFormatter

fun Result<Record>.toOsmsBooking(
    bookingTable: Booking, 
    branchTable: Branch, 
    floorTable: Floor,
    officeTable: Office,
    permissionTable: Permission,
) = if (isNotEmpty) {
        OsmsBooking(
            OsmsBookingUid(getValues(bookingTable.BOOKING_UID)[0]!!),
            OsmsUserUid(getValues(bookingTable.USER_UID)[0]!!),
            OsmsWorkspaceUid(getValues(bookingTable.WORKPLACE_UID)[0]!!),
            OsmsBranch(
                OsmsBranchUid(getValues(bookingTable.BRANCH_UID)[0]!!),
                getValues(branchTable.NAME)[0]!!,
                getValues(branchTable.DESCRIPTION)[0] ?: "",
            ),
            OsmsFloor(
                OsmsFloorUid(getValues(bookingTable.FLOOR_UID)[0]!!),
                getValues(floorTable.LEVEL)[0]!!,
                getValues(floorTable.DESCRIPTION)[0] ?: "",
            ),
            OsmsOffice(
                OsmsOfficeUid(getValues(bookingTable.OFFICE_UID)[0]!!),
                getValues(officeTable.NAME)[0]!!,
                getValues(officeTable.DESCRIPTION)[0] ?: "",
            ),
            getValues(bookingTable.DESCRIPTION)[0] ?: "",
            getValues(bookingTable.STARTTIME)[0]!!.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            getValues(bookingTable.ENDTIME)[0]!!.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            getValues(bookingTable.LOCK)[0]?.let { OsmsBookingLock(it) } ?: OsmsBookingLock.NONE,
            getValues(permissionTable.NAME).map {
                ru.otus.osms.common.models.OsmsBookingPermissions.valueOf(it!!)
            }.toSet()
        )
    } else { null }

fun Result<Record>.toOsmsBookings(
    bookingTable: Booking,
    branchTable: Branch,
    floorTable: Floor,
    officeTable: Office,
): List<OsmsBooking> = this.map { record ->
    OsmsBooking(
        OsmsBookingUid(record.getValue(bookingTable.BOOKING_UID)!!),
        OsmsUserUid(record.getValue(bookingTable.USER_UID)!!),
        OsmsWorkspaceUid(record.getValue(bookingTable.WORKPLACE_UID)!!),
        OsmsBranch(
            OsmsBranchUid(record.getValue(bookingTable.BRANCH_UID)!!),
            record.getValue(branchTable.NAME)!!,
            record.getValue(branchTable.DESCRIPTION) ?: "",
        ),
        OsmsFloor(
            OsmsFloorUid(record.getValue(bookingTable.FLOOR_UID)!!),
            record.getValue(floorTable.LEVEL)!!,
            record.getValue(floorTable.DESCRIPTION) ?: "",
        ),
        OsmsOffice(
            OsmsOfficeUid(record.getValue(bookingTable.OFFICE_UID)!!),
            record.getValue(officeTable.NAME)!!,
            record.getValue(floorTable.DESCRIPTION) ?: ""
        ),
        record.getValue(bookingTable.DESCRIPTION) ?: "",
        record.getValue(bookingTable.STARTTIME)!!.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        record.getValue(bookingTable.ENDTIME)!!.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        record.getValue(bookingTable.LOCK)?.let { OsmsBookingLock(it) } ?: OsmsBookingLock.NONE,
    )
}