package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.assertEquals

fun validationUpdateUidIsBlank(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
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
            lock = OsmsBookingLock("123"),
        ),
    )
    processor.exec(context)

    assertEquals(6, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
}

fun validationCreateUidIsBlank(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
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

    processor.exec(context)

    assertEquals(5, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
}

fun validationReadUidIsBlank(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid.NONE,
            lock = OsmsBookingLock("123")
        ),
    )
    processor.exec(context)

    assertEquals(1, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
    assertEquals(context.errors.firstOrNull()?.group, "validation")
}
