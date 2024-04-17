package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.assertEquals

fun validationBlankTimeAndFormatTime(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid(VALID_UID),
            userUid = OsmsUserUid(VALID_UID),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(VALID_UID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(VALID_UID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(VALID_UID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(VALID_UID),
            startTime = "",
            endTime = "9999-99-99T99:99:99.9999",
        ),
    )
    processor.exec(ctx)

    assertEquals(2, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}

fun validationTimeIsInvalid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid(VALID_UID),
            userUid = OsmsUserUid(VALID_UID),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(VALID_UID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(VALID_UID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(VALID_UID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(VALID_UID),
            startTime = "2024-02-01T10:00:00",
            endTime = "2024-01-01T10:00:00",
        ),
    )
    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}
