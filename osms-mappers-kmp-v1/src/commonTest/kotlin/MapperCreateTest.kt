package ru.otus.osms.mappers.kmp.v1.test

import ru.otus.osms.api.v1.kpm.models.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.mappers.kmp.v1.fromTransport
import ru.otus.osms.mappers.kmp.v1.toTransportBooking
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperCreateTest {
    @Test
    fun fromTransport() {
        val request = BookingCreateRequest(
            requestUid = REQUEST_UID,
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingCreateObject(
                userUid = "user-1",
                workspaceUid = "workspace-1",
                branch = Branch("branch-1", "Branch"),
                floor = Floor("floor-1", "1", "Floor"),
                office = Office("office-1", "Office"),
                description = "Test",
                startTime = "2024-01-01T10:00:00",
                endTime = "2024-01-01T12:00:00",
            ),
        )
        val context = OsmsContext()
        
        context.fromTransport(request)

        assertEquals(OsmsStub.SUCCESS, context.stubCase)
        assertEquals(OsmsWorkMode.STUB, context.workMode)
        assertEquals(request.booking?.userUid, context.bookingRequest.userUid.asString())
        assertEquals(request.booking?.workspaceUid, context.bookingRequest.workspaceUid.asString())
        assertEquals(request.booking?.branch?.branchUid, context.bookingRequest.branch.branchUid.asString())
        assertEquals(request.booking?.floor?.floorUid, context.bookingRequest.floor.floorUid.asString())
        assertEquals(request.booking?.office?.officeUid, context.bookingRequest.office.officeUid.asString())
        assertEquals(request.booking?.description, context.bookingRequest.description)
        assertEquals(request.booking?.startTime, context.bookingRequest.startTime)
        assertEquals(request.booking?.endTime, context.bookingRequest.endTime)
    }

    @Test
    fun toTransport() {
        val context = OsmsContext(
            requestUid = OsmsRequestUid(REQUEST_UID),
            command = OsmsCommand.CREATE,
            bookingRequest = OsmsBooking(
                bookingUid = OsmsBookingUid("!@#/$%^&*"),
                userUid = OsmsUserUid("user-1"),
                workspaceUid = OsmsWorkspaceUid("workspace-1"),
                branch = OsmsBranch(OsmsBranchUid("branch-1"), "Branch"),
                floor = OsmsFloor(OsmsFloorUid("floor-1"), "1", "Floor"),
                office = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
                description = "Test",
                startTime = "2024-01-01T10:00:00",
                endTime = "2024-01-01T12:00:00",
            ),
            errors = mutableListOf(
                OsmsError(
                    code = "err",
                    group = "request",
                    field = "uid",
                    message = "wrong uid",
                )
            ),
            state = OsmsState.RUNNING,
        )

        val request = context.toTransportBooking() as BookingCreateResponse

        assertEquals(REQUEST_UID, request.requestUid)
        assertEquals(1, request.errors?.size)
        assertEquals(context.errors.firstOrNull()?.code, request.errors?.firstOrNull()?.code)
        assertEquals(context.errors.firstOrNull()?.group, request.errors?.firstOrNull()?.group)
        assertEquals(context.errors.firstOrNull()?.field, request.errors?.firstOrNull()?.field)
        assertEquals(context.errors.firstOrNull()?.message, request.errors?.firstOrNull()?.message)
    }
    
    companion object {
        private const val REQUEST_UID = "request-1"
    }
}