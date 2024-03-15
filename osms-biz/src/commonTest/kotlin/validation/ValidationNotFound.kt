package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.assertEquals

fun validationUpdateUidIsBlank(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid(""),
            userUid = OsmsUserUid(""),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(""),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid.NONE,
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid.NONE,
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid.NONE,
            startTime = "2023-12-21T10:00:00",
            endTime = "2023-12-21T16:00:00",
        ),
    )
    processor.exec(ctx)

    assertEquals(6, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}

fun validationCreateUidIsBlank(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            userUid = OsmsUserUid(""),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid(""),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid.NONE,
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid.NONE,
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid.NONE,
            startTime = "2023-12-21T10:00:00",
            endTime = "2023-12-21T16:00:00",
        ),
    )
    processor.exec(ctx)

    assertEquals(5, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}

fun validationReadUidIsBlank(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val ctx = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid.NONE,
        ),
    )
    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(OsmsState.FAILING, ctx.state)
}
