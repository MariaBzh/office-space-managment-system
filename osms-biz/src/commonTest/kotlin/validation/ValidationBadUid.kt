package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.assertEquals

fun validationUpdateUidIsNotValid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
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
            lock = OsmsBookingLock(LOCK),
        ),
    )
    processor.exec(context)

    assertEquals(6, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
}

fun validationCreateUidIsNotValid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
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
            lock = OsmsBookingLock(LOCK),
        ),
    )
    processor.exec(context)

    assertEquals(5, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
}

fun validationReadUidIsNotValid(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid(INVALID_UID),
            lock = OsmsBookingLock("123")
        ),
    )
    processor.exec(context)

    assertEquals(1, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
    assertEquals(context.errors.firstOrNull()?.group, "validation")
}

private const val LOCK = "123"
