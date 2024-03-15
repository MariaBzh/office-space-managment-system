package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.assertEquals

fun validationUpdateUidIsNotValid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid(INVALID_UID),
            userUid = OsmsUserUid(INVALID_UID),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(INVALID_UID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(INVALID_UID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(INVALID_UID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(INVALID_UID),
            startTime = "2023-12-21T10:00:00",
            endTime = "2023-12-21T16:00:00",
        ),
    )
    processor.exec(ctx)

    assertEquals(6, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}

fun validationCreateUidIsNotValid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            userUid = OsmsUserUid(INVALID_UID),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(INVALID_UID),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid(INVALID_UID),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid(INVALID_UID),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid(INVALID_UID),
            startTime = "2023-12-21T10:00:00",
            endTime = "2023-12-21T16:00:00",
        ),
    )
    processor.exec(ctx)

    assertEquals(5, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}

fun validationReadUidIsNotValid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid(INVALID_UID),
        ),
    )
    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}
