package ru.otus.osms.stubs

import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.stubs.OsmsBookingStubBolts.BOOKING_DB_ERROR
import ru.otus.osms.stubs.OsmsBookingStubBolts.BOOKING_ERROR_BAD_TIME
import ru.otus.osms.stubs.OsmsBookingStubBolts.BOOKING_ERROR_BAD_UID
import ru.otus.osms.stubs.OsmsBookingStubBolts.BOOKING_ERROR_NOT_FOUND
import ru.otus.osms.stubs.OsmsBookingStubBolts.BOOKING_READ
import ru.otus.osms.stubs.OsmsBookingStubBolts.BRANCH_A
import ru.otus.osms.stubs.OsmsBookingStubBolts.BRANCH_B
import ru.otus.osms.stubs.OsmsBookingStubBolts.BRANCH_C
import ru.otus.osms.stubs.OsmsBookingStubBolts.FLOOR_1
import ru.otus.osms.stubs.OsmsBookingStubBolts.FLOOR_2
import ru.otus.osms.stubs.OsmsBookingStubBolts.FLOOR_3

object OsmsBookingStub {

    fun get(): OsmsBooking = BOOKING_READ.copy()

    fun get(bookingUid: String): OsmsBooking = BOOKING_READ.copy(
        bookingUid = OsmsBookingUid(bookingUid)
    )

    fun get(bookingUid: String, startTime: String, endTime: String): OsmsBooking = BOOKING_READ.copy(
        bookingUid = OsmsBookingUid(bookingUid),
        startTime = startTime,
        endTime = endTime,
    )

    fun getError(code: String): OsmsError =
        when (code) {
            OsmsStub.NOT_FOUND.name -> BOOKING_ERROR_NOT_FOUND.copy()
            OsmsStub.BAD_UID.name -> BOOKING_ERROR_BAD_UID.copy()
            OsmsStub.BAD_TIME.name -> BOOKING_ERROR_BAD_TIME.copy()
            OsmsStub.DB_ERROR.name -> BOOKING_DB_ERROR.copy()
            else -> { BOOKING_ERROR_NOT_FOUND.copy() }
        }


    fun prepareResult(block: OsmsBooking.() -> Unit): OsmsBooking = get().apply(block)

    fun prepareSearchList(
        userUid: String? = "user-1",
    ) = listOf(
        osmsBooking(
            base = BOOKING_READ,
            bookingUid = "booking-1",
            userUid = "user-1",
            workspaceUid = "workspace-1",
            branch = BRANCH_A,
            floor = FLOOR_1,
            startTime = "2024-01-01T10:00:00.0000",
            endTime = "2024-01-01T10:40:00.0000"
        ),
        osmsBooking(
            base = BOOKING_READ,
            bookingUid = "booking-2",
            userUid = "user-2",
            workspaceUid = "workspace-2",
            branch = BRANCH_B,
            floor = FLOOR_1,
            startTime = "2024-01-01T11:30:00.0000",
            endTime = "2024-01-01T13:30:00.0000"
        ),
        osmsBooking(
            base = BOOKING_READ,
            bookingUid = "booking-3",
            userUid = "user-3",
            workspaceUid = "workspace-3",
            branch = BRANCH_C,
            floor = FLOOR_2,
            startTime = "2024-01-01T09:40:00.0000",
            endTime = "2024-01-01T11:45:00.0000",
        ),
        osmsBooking(
            base = BOOKING_READ,
            bookingUid = "booking-4",
            userUid = "user-2",
            workspaceUid = "workspace-2",
            branch = BRANCH_B,
            floor = FLOOR_1,
            startTime = "2024-01-01T14:35:00.0000",
            endTime = "2024-01-01T15:35:00.0000",
        ),
        osmsBooking(
            base = BOOKING_READ,
            bookingUid = "booking-5",
            userUid = "user-4",
            workspaceUid = "workspace-4",
            branch = BRANCH_A,
            floor = FLOOR_2,
            startTime = "2024-01-01T16:00:00.0000",
            endTime = "2024-01-01T17:00:00.0000",
        ),
        osmsBooking(
            base = BOOKING_READ,
            bookingUid = "booking-6",
            userUid = "user-3",
            workspaceUid = "workspace-5",
            branch = BRANCH_C,
            floor = FLOOR_3,
            startTime = "2024-01-01T11:45:00.0000",
            endTime = "2024-01-01T14:55:00.0000",
        ),
    ).filter { it.userUid.asString() == userUid }.toList()

    private fun osmsBooking(
        base: OsmsBooking,
        bookingUid: String,
        userUid: String,
        workspaceUid: String,
        branch: OsmsBranch,
        floor: OsmsFloor,
        startTime: String,
        endTime: String,
    ) = base.copy(
        bookingUid = OsmsBookingUid(bookingUid),
        userUid = OsmsUserUid(userUid),
        workspaceUid = OsmsWorkspaceUid(workspaceUid),
        branch = branch,
        floor = floor,
        startTime = startTime,
        endTime = endTime,
    )
}