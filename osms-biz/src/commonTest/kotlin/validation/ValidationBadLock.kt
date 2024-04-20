package ru.otus.osms.biz.test.validation

import kotlinx.coroutines.test.runTest
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid("123"),
            userUid = OsmsUserUid("user-1"),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid("123"),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid("123"),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid("123"),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid("123"),
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
            lock = OsmsBookingLock(LOCK),
        ),
    )

    processor.exec(context)

    assertEquals(0, context.errors.size)
    assertNotEquals(OsmsState.FAILING, context.state)
}

fun validationLockTrim(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid("123"),
            userUid = OsmsUserUid("user-1"),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid("123"),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid("123"),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid("123"),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid("123"),
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
            lock = OsmsBookingLock("\n\t $LOCK \n\t"),
        ),
    )

    processor.exec(context)

    assertEquals(1, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)
    assertEquals(context.errors.firstOrNull()?.group, "validation")
}

fun validationLockEmpty(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid("123"),
            userUid = OsmsUserUid("user-1"),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid("123"),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid("123"),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid("123"),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid("123"),
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
            lock = OsmsBookingLock(""),
        ),
    )

    processor.exec(context)

    assertEquals(1, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)

    val error = context.errors.firstOrNull()

    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: OsmsCommand, processor: OsmsBookingProcessor) = runTest {
    val context = OsmsContext(
        command = command,
        state = OsmsState.NONE,
        workMode = OsmsWorkMode.TEST,
        bookingRequest = OsmsBooking(
            bookingUid = OsmsBookingUid("123"),
            userUid = OsmsUserUid("user-1"),
            branch = OsmsBranch(
                branchUid = OsmsBranchUid("123"),
                name = "Московский",
            ),
            floor = OsmsFloor(
                floorUid = OsmsFloorUid("123"),
                level = "1A",
            ),
            office = OsmsOffice(
                officeUid = OsmsOfficeUid("123"),
                name = "Марс"
            ),
            workspaceUid = OsmsWorkspaceUid("123"),
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
            lock = OsmsBookingLock("!@#\$%^&*(),.{}"),
        ),
    )

    processor.exec(context)

    assertEquals(1, context.errors.size)
    assertEquals(OsmsState.FAILING, context.state)

    val error = context.errors.firstOrNull()
    
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

private const val LOCK = "123"