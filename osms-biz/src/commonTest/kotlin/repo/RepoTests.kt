package ru.otus.osms.biz.test.repo

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingResponse
import ru.otus.osms.repo.test.BookingRepositoryMock
import kotlin.test.assertEquals

fun repoNotFoundTest(command: OsmsCommand) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid("123"),
            userUid = OsmsUserUid(UUID),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(UUID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(UUID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(UUID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(UUID),
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
            lock = OsmsBookingLock(LOCK),
        ),
    )

    PROCESSOR.exec(context)

    assertEquals(OsmsState.FAILING, context.state)
    assertEquals(OsmsBooking(), context.bookingResponse)
    assertEquals(1, context.errors.size)
    assertEquals("bookingUid", context.errors.first().field)
}

private val USER_UID = OsmsUserUid("user-1")

private const val UUID = "2d0ff2ad-11se-df12-9y0q-we11a0xz45g1"
private const val LOCK = "123"

private val INIT_BOOKING = OsmsBooking(
    bookingUid = OsmsBookingUid(UUID),
    userUid = USER_UID,
    branch = OsmsBranch(
        branchUid = OsmsBranchUid(UUID),
        name = "Московский",
    ),
    floor = OsmsFloor(
        floorUid = OsmsFloorUid(UUID),
        level = "1A",
    ),
    office = OsmsOffice(
        officeUid = OsmsOfficeUid(UUID),
        name = "Марс"
    ),
    workspaceUid = OsmsWorkspaceUid(UUID),
    startTime = "2024-01-01T10:00:00",
    endTime = "2024-01-01T12:00:00",
)
private val REPO by lazy {
    BookingRepositoryMock(
        invokeReadBooking = {
            if (it.bookingUid == INIT_BOOKING.bookingUid) {
                DbBookingResponse(
                    isSuccess = true,
                    data = INIT_BOOKING,
                )
            } else DbBookingResponse(
                isSuccess = false,
                data = null,
                errors = listOf(OsmsError(message = "Not found", field = "bookingUid"))
            )
        },
    )
}
private val SETTINGS by lazy {
    OsmsCorSettings(
        repoTest = REPO
    )
}
private val PROCESSOR by lazy {
    OsmsBookingProcessor(SETTINGS)
}