package ru.otus.osms.mappers.jackson.v1.test

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.otus.osms.api.v1.models.*
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.common.models.*
import ru.otus.osms.common.stubs.OsmsStub
import ru.otus.osms.mappers.jackson.v1.fromTransport
import ru.otus.osms.mappers.jackson.v1.toTransportBooking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class MapperTest {
    @ParameterizedTest(name = "{index} {0} serialized")
    @MethodSource("listOfRequests")
    fun fromTransport(request: IRequest) {
        val context = OsmsContext()

        context.fromTransport(request)

        when(request) {
            is BookingCreateRequest -> {
                assertEquals(request.requestUid, context.requestUid.asString())
                assertEquals(OsmsStub.SUCCESS, context.stubCase)
                assertEquals(OsmsWorkMode.STUB, context.workMode)

                assertEquals(request.booking?.userUid, context.bookingRequest.userUid.asString())

                assertEquals(request.booking?.branch?.branchUid, context.bookingRequest.branch.branchUid.asString())
                assertEquals(request.booking?.branch?.name, context.bookingRequest.branch.name)

                assertEquals(request.booking?.floor?.floorUid, context.bookingRequest.floor.floorUid.asString())
                assertEquals(request.booking?.floor?.level, context.bookingRequest.floor.level)

                assertEquals(request.booking?.office?.officeUid, context.bookingRequest.office.officeUid.asString())
                assertEquals(request.booking?.office?.name, context.bookingRequest.office.name)

                assertEquals(request.booking?.workspaceUid, context.bookingRequest.workspaceUid.asString())
                assertEquals(request.booking?.startTime, context.bookingRequest.startTime)
                assertEquals(request.booking?.endTime, context.bookingRequest.endTime)
            }
            is BookingReadRequest -> {
                assertEquals(request.requestUid, context.requestUid.asString())
                assertEquals(OsmsStub.SUCCESS, context.stubCase)
                assertEquals(OsmsWorkMode.STUB, context.workMode)

                assertEquals(request.booking?.bookingUid, context.bookingRequest.bookingUid.asString())
            }
            is BookingUpdateRequest -> {
                assertEquals(request.requestUid, context.requestUid.asString())
                assertEquals(OsmsStub.SUCCESS, context.stubCase)
                assertEquals(OsmsWorkMode.STUB, context.workMode)

                assertEquals(request.booking?.bookingUid, context.bookingRequest.bookingUid.asString())

                assertEquals(request.booking?.userUid, context.bookingRequest.userUid.asString())

                assertEquals(request.booking?.branch?.branchUid, context.bookingRequest.branch.branchUid.asString())
                assertEquals(request.booking?.branch?.name, context.bookingRequest.branch.name)

                assertEquals(request.booking?.floor?.floorUid, context.bookingRequest.floor.floorUid.asString())
                assertEquals(request.booking?.floor?.level, context.bookingRequest.floor.level)

                assertEquals(request.booking?.office?.officeUid, context.bookingRequest.office.officeUid.asString())
                assertEquals(request.booking?.office?.name, context.bookingRequest.office.name)

                assertEquals(request.booking?.workspaceUid, context.bookingRequest.workspaceUid.asString())
                assertEquals(request.booking?.startTime, context.bookingRequest.startTime)
                assertEquals(request.booking?.endTime, context.bookingRequest.endTime)
            }
            is  BookingDeleteRequest -> {
                assertEquals(request.requestUid, context.requestUid.asString())
                assertEquals(OsmsStub.SUCCESS, context.stubCase)
                assertEquals(OsmsWorkMode.STUB, context.workMode)

                assertEquals(request.booking?.bookingUid, context.bookingRequest.bookingUid.asString())
            }
            is BookingSearchRequest -> {
                assertEquals(request.requestUid, context.requestUid.asString())
                assertEquals(OsmsStub.SUCCESS, context.stubCase)
                assertEquals(OsmsWorkMode.STUB, context.workMode)

                assertEquals(request.bookingFilter?.userUid, context.bookingFilterRequest.userUid.asString())

                assertEquals(request.bookingFilter?.branch?.branchUid, context.bookingFilterRequest.branch.branchUid.asString())
                assertEquals(request.bookingFilter?.branch?.name, context.bookingFilterRequest.branch.name)

                assertEquals(request.bookingFilter?.floor?.floorUid, context.bookingFilterRequest.floor.floorUid.asString())
                assertEquals(request.bookingFilter?.floor?.level, context.bookingFilterRequest.floor.level)

                assertEquals(request.bookingFilter?.office?.officeUid, context.bookingFilterRequest.office.officeUid.asString())
                assertEquals(request.bookingFilter?.office?.name, context.bookingFilterRequest.office.name)

                assertEquals(request.bookingFilter?.workspaceUid, context.bookingFilterRequest.workspaceUid.asString())
                assertEquals(request.bookingFilter?.startTime, context.bookingFilterRequest.startTime)
                assertEquals(request.bookingFilter?.endTime, context.bookingFilterRequest.endTime)
            }
        }
    }

    @ParameterizedTest(name = "{index} {0} serialized")
    @MethodSource("listOfContexts")
    fun toTransport(context: OsmsContext) {
        when (val request = context.toTransportBooking()) {
            is BookingCreateResponse -> {
                assertEquals(context.requestUid.asString(), request.requestUid)
                assertEquals(ResponseResult.SUCCESS, request.result)

                assertEquals(context.bookingResponse.userUid.asString(), request.booking?.userUid)

                assertEquals(context.bookingResponse.branch.branchUid.asString(), request.booking?.branch?.branchUid)
                assertEquals(context.bookingResponse.branch.name, request.booking?.branch?.name)

                assertEquals(context.bookingResponse.floor.floorUid.asString(), request.booking?.floor?.floorUid)
                assertEquals(context.bookingResponse.floor.level, request.booking?.floor?.level)

                assertEquals(context.bookingResponse.office.officeUid.asString(), request.booking?.office?.officeUid)
                assertEquals(context.bookingResponse.office.name, request.booking?.office?.name)

                assertEquals(context.bookingResponse.workspaceUid.asString(), request.booking?.workspaceUid)
                assertEquals(context.bookingResponse.startTime, request.booking?.startTime)
                assertEquals(context.bookingResponse.endTime, request.booking?.endTime)

                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.READ) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.UPDATE) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.DELETE) }
            }
            is BookingReadResponse -> {
                assertEquals(context.requestUid.asString(), request.requestUid)
                assertEquals(ResponseResult.SUCCESS, request.result)

                assertEquals(context.bookingResponse.bookingUid.asString(), request.booking?.bookingUid)

                assertEquals(context.bookingResponse.userUid.asString(), request.booking?.userUid)

                assertEquals(context.bookingResponse.branch.branchUid.asString(), request.booking?.branch?.branchUid)
                assertEquals(context.bookingResponse.branch.name, request.booking?.branch?.name)

                assertEquals(context.bookingResponse.floor.floorUid.asString(), request.booking?.floor?.floorUid)
                assertEquals(context.bookingResponse.floor.level, request.booking?.floor?.level)

                assertEquals(context.bookingResponse.office.officeUid.asString(), request.booking?.office?.officeUid)
                assertEquals(context.bookingResponse.office.name, request.booking?.office?.name)

                assertEquals(context.bookingResponse.workspaceUid.asString(), request.booking?.workspaceUid)
                assertEquals(context.bookingResponse.startTime, request.booking?.startTime)
                assertEquals(context.bookingResponse.endTime, request.booking?.endTime)

                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.READ) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.UPDATE) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.DELETE) }
            }
            is BookingUpdateResponse -> {
                assertEquals(context.requestUid.asString(), request.requestUid)
                assertEquals(ResponseResult.SUCCESS, request.result)

                assertEquals(context.bookingResponse.bookingUid.asString(), request.booking?.bookingUid)

                assertEquals(context.bookingResponse.userUid.asString(), request.booking?.userUid)

                assertEquals(context.bookingResponse.branch.branchUid.asString(), request.booking?.branch?.branchUid)
                assertEquals(context.bookingResponse.branch.name, request.booking?.branch?.name)

                assertEquals(context.bookingResponse.floor.floorUid.asString(), request.booking?.floor?.floorUid)
                assertEquals(context.bookingResponse.floor.level, request.booking?.floor?.level)

                assertEquals(context.bookingResponse.office.officeUid.asString(), request.booking?.office?.officeUid)
                assertEquals(context.bookingResponse.office.name, request.booking?.office?.name)

                assertEquals(context.bookingResponse.workspaceUid.asString(), request.booking?.workspaceUid)
                assertEquals(context.bookingResponse.startTime, request.booking?.startTime)
                assertEquals(context.bookingResponse.endTime, request.booking?.endTime)

                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.READ) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.UPDATE) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.DELETE) }
            }
            is BookingDeleteResponse -> {
                assertEquals(context.requestUid.asString(), request.requestUid)
                assertEquals(ResponseResult.SUCCESS, request.result)

                assertEquals(context.bookingResponse.bookingUid.asString(), request.booking?.bookingUid)

                assertEquals(context.bookingResponse.userUid.asString(), request.booking?.userUid)

                assertEquals(context.bookingResponse.branch.branchUid.asString(), request.booking?.branch?.branchUid)
                assertEquals(context.bookingResponse.branch.name, request.booking?.branch?.name)

                assertEquals(context.bookingResponse.floor.floorUid.asString(), request.booking?.floor?.floorUid)
                assertEquals(context.bookingResponse.floor.level, request.booking?.floor?.level)

                assertEquals(context.bookingResponse.office.officeUid.asString(), request.booking?.office?.officeUid)
                assertEquals(context.bookingResponse.office.name, request.booking?.office?.name)

                assertEquals(context.bookingResponse.workspaceUid.asString(), request.booking?.workspaceUid)
                assertEquals(context.bookingResponse.startTime, request.booking?.startTime)
                assertEquals(context.bookingResponse.endTime, request.booking?.endTime)

                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.READ) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.UPDATE) }
                request.booking?.permissions?.let { assertContains(it.asIterable(), BookingPermissions.DELETE) }
            }
            is BookingSearchResponse -> {
                assertEquals(request.requestUid, context.requestUid.asString())
                assertEquals(ResponseResult.SUCCESS, request.result)

                assertEquals(request.bookings?.firstOrNull()?.bookingUid, context.bookingsResponse.firstOrNull()?.bookingUid?.asString())

                assertEquals(request.bookings?.firstOrNull()?.userUid, context.bookingsResponse.firstOrNull()?.userUid?.asString())

                assertEquals(request.bookings?.firstOrNull()?.branch?.branchUid, context.bookingsResponse.firstOrNull()?.branch?.branchUid?.asString())
                assertEquals(request.bookings?.firstOrNull()?.branch?.name, context.bookingsResponse.firstOrNull()?.branch?.name)

                assertEquals(request.bookings?.firstOrNull()?.floor?.floorUid, context.bookingsResponse.firstOrNull()?.floor?.floorUid?.asString())
                assertEquals(request.bookings?.firstOrNull()?.floor?.level, context.bookingsResponse.firstOrNull()?.floor?.level)

                assertEquals(request.bookings?.firstOrNull()?.office?.officeUid, context.bookingsResponse.firstOrNull()?.office?.officeUid?.asString())
                assertEquals(request.bookings?.firstOrNull()?.office?.name, context.bookingsResponse.firstOrNull()?.office?.name)

                assertEquals(request.bookings?.firstOrNull()?.workspaceUid, context.bookingsResponse.firstOrNull()?.workspaceUid?.asString())
                assertEquals(request.bookings?.firstOrNull()?.startTime, context.bookingsResponse.firstOrNull()?.startTime)
                assertEquals(request.bookings?.firstOrNull()?.endTime, context.bookingsResponse.firstOrNull()?.endTime)
            }
        }

    }

    @Test
    fun toTransportFailed() {
        val request = readWithErrorContext.toTransportBooking() as BookingCreateResponse

        assertEquals(request.requestUid, readWithErrorContext.requestUid.asString())
        assertEquals(ResponseResult.ERROR, request.result)

        assertEquals(request.errors?.firstOrNull()?.code, readWithErrorContext.errors.firstOrNull()?.code)
        assertEquals(request.errors?.firstOrNull()?.group, readWithErrorContext.errors.firstOrNull()?.group)
        assertEquals(request.errors?.firstOrNull()?.message, readWithErrorContext.errors.firstOrNull()?.message)
    }

    companion object {
        private val createRequest = BookingCreateRequest(
            requestUid = UUID.randomUUID().toString(),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingCreateObject(
                userUid = UUID.randomUUID().toString(),
                branch = Branch(
                    branchUid = UUID.randomUUID().toString(),
                    name = "Московский",
                ),
                floor = Floor(
                    floorUid = UUID.randomUUID().toString(),
                    level = "1A",
                ),
                office = Office(
                    officeUid = UUID.randomUUID().toString(),
                    name = "Марс"
                ),
                workspaceUid = UUID.randomUUID().toString(),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
            )
        )

        private val readRequest = BookingReadRequest(
            requestUid = UUID.randomUUID().toString(),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingReadObject(
                bookingUid = UUID.randomUUID().toString(),
            )
        )

        private val updateRequest = BookingUpdateRequest(
            requestUid = UUID.randomUUID().toString(),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingUpdateObject(
                bookingUid = UUID.randomUUID().toString(),
                userUid = UUID.randomUUID().toString(),
                branch = Branch(
                    branchUid = UUID.randomUUID().toString(),
                    name = "Московский",
                ),
                floor = Floor(
                    floorUid = UUID.randomUUID().toString(),
                    level = "1A",
                ),
                office = Office(
                    officeUid = UUID.randomUUID().toString(),
                    name = "Марс"
                ),
                workspaceUid = UUID.randomUUID().toString(),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
            )
        )

        private val deleteRequest = BookingDeleteRequest(
            requestUid = UUID.randomUUID().toString(),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingDeleteObject(
                bookingUid = UUID.randomUUID().toString(),
            )
        )

        private val searchRequest = BookingSearchRequest(
            requestUid = UUID.randomUUID().toString(),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            bookingFilter = BookingSearchFilter(
                userUid = UUID.randomUUID().toString(),
                branch = Branch(
                    branchUid = UUID.randomUUID().toString(),
                    name = "Московский",
                ),
                floor = Floor(
                    floorUid = UUID.randomUUID().toString(),
                    level = "1A",
                ),
                office = Office(
                    officeUid = UUID.randomUUID().toString(),
                    name = "Марс"
                ),
                workspaceUid = UUID.randomUUID().toString(),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
            )
        )

        @JvmStatic
        private fun listOfRequests() = listOf(
            createRequest,
            readRequest,
            updateRequest,
            deleteRequest,
            searchRequest,
        )

        private val createContext = OsmsContext(
            requestUid = OsmsRequestUid(UUID.randomUUID().toString()),
            workMode = OsmsWorkMode.STUB,
            command = OsmsCommand.CREATE,
            bookingResponse = OsmsBooking(
                userUid = OsmsUserUid(UUID.randomUUID().toString()),
                workspaceUid = OsmsWorkspaceUid(UUID.randomUUID().toString()),
                branch = OsmsBranch(
                    branchUid = OsmsBranchUid(UUID.randomUUID().toString()),
                    name = "Московский",
                ),
                floor = OsmsFloor(
                    floorUid = OsmsFloorUid(UUID.randomUUID().toString()),
                    level = "1A",
                ),
                office = OsmsOffice(
                    officeUid = OsmsOfficeUid(UUID.randomUUID().toString()),
                    name = "Марс"
                ),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
                permissions = mutableSetOf(
                    OsmsBookingPermissions.READ,
                    OsmsBookingPermissions.UPDATE,
                    OsmsBookingPermissions.DELETE,
                ),
            ),
            state = OsmsState.RUNNING,
        )

        private val readWithErrorContext = OsmsContext(
            requestUid = OsmsRequestUid("a23ffq17-4dfg-3d41-gg18-46f23rt201so"),
            workMode = OsmsWorkMode.STUB,
            command = OsmsCommand.READ,
            errors = mutableListOf(
                OsmsError(
                    code = "404",
                    group = "Error",
                    message = "Not Found"
                )
            ),
            state = OsmsState.FAILING,
        )

        private val readContext = OsmsContext(
            requestUid = OsmsRequestUid(UUID.randomUUID().toString()),
            workMode = OsmsWorkMode.STUB,
            command = OsmsCommand.READ,
            bookingResponse = OsmsBooking(
                bookingUid = OsmsBookingUid(UUID.randomUUID().toString()),
                userUid = OsmsUserUid(UUID.randomUUID().toString()),
                workspaceUid = OsmsWorkspaceUid(UUID.randomUUID().toString()),
                branch = OsmsBranch(
                    branchUid = OsmsBranchUid(UUID.randomUUID().toString()),
                    name = "Московский",
                ),
                floor = OsmsFloor(
                    floorUid = OsmsFloorUid(UUID.randomUUID().toString()),
                    level = "1A",
                ),
                office = OsmsOffice(
                    officeUid = OsmsOfficeUid(UUID.randomUUID().toString()),
                    name = "Марс"
                ),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
                permissions = mutableSetOf(
                    OsmsBookingPermissions.READ,
                    OsmsBookingPermissions.UPDATE,
                    OsmsBookingPermissions.DELETE,
                ),
            ),
            state = OsmsState.RUNNING,
        )

        private val updateContext = OsmsContext(
            requestUid = OsmsRequestUid(UUID.randomUUID().toString()),
            workMode = OsmsWorkMode.STUB,
            command = OsmsCommand.UPDATE,
            bookingResponse = OsmsBooking(
                bookingUid = OsmsBookingUid(UUID.randomUUID().toString()),
                userUid = OsmsUserUid(UUID.randomUUID().toString()),
                workspaceUid = OsmsWorkspaceUid(UUID.randomUUID().toString()),
                branch = OsmsBranch(
                    branchUid = OsmsBranchUid(UUID.randomUUID().toString()),
                    name = "Московский",
                ),
                floor = OsmsFloor(
                    floorUid = OsmsFloorUid(UUID.randomUUID().toString()),
                    level = "1A",
                ),
                office = OsmsOffice(
                    officeUid = OsmsOfficeUid(UUID.randomUUID().toString()),
                    name = "Марс"
                ),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
                permissions = mutableSetOf(
                    OsmsBookingPermissions.READ,
                    OsmsBookingPermissions.UPDATE,
                    OsmsBookingPermissions.DELETE,
                ),
            ),
            state = OsmsState.RUNNING,
        )

        private val deleteContext = OsmsContext(
            requestUid = OsmsRequestUid(UUID.randomUUID().toString()),
            workMode = OsmsWorkMode.STUB,
            command = OsmsCommand.DELETE,
            bookingResponse = OsmsBooking(
                bookingUid = OsmsBookingUid(UUID.randomUUID().toString()),
                userUid = OsmsUserUid(UUID.randomUUID().toString()),
                workspaceUid = OsmsWorkspaceUid(UUID.randomUUID().toString()),
                branch = OsmsBranch(
                    branchUid = OsmsBranchUid(UUID.randomUUID().toString()),
                    name = "Московский",
                ),
                floor = OsmsFloor(
                    floorUid = OsmsFloorUid(UUID.randomUUID().toString()),
                    level = "1A",
                ),
                office = OsmsOffice(
                    officeUid = OsmsOfficeUid(UUID.randomUUID().toString()),
                    name = "Марс"
                ),
                startTime = "2023-12-21T10:00:00.000",
                endTime = "2023-12-21T16:00:00.000",
                permissions = mutableSetOf(
                    OsmsBookingPermissions.READ,
                    OsmsBookingPermissions.UPDATE,
                    OsmsBookingPermissions.DELETE,
                ),
            ),
            state = OsmsState.RUNNING,
        )

        private val searchContext = OsmsContext(
            requestUid = OsmsRequestUid(UUID.randomUUID().toString()),
            workMode = OsmsWorkMode.STUB,
            command = OsmsCommand.SEARCH,
            bookingsResponse = mutableListOf(
                OsmsBooking(
                    bookingUid = OsmsBookingUid("d93ww1as-6tf2-4f81-99a8-8ae2fhb229e2"),
                    userUid = OsmsUserUid("f947b0f7-5df1-4580-8848-88e245b219a2"),
                    workspaceUid = OsmsWorkspaceUid("3d6acb16-6e60-42ca-8fd3-a7492fa21caa"),
                    branch = OsmsBranch(
                        branchUid = OsmsBranchUid("2cc01441-4d6e-48de-a56d-cacc0e32f5a9"),
                        name = "Московский",
                    ),
                    floor = OsmsFloor(
                        floorUid = OsmsFloorUid("542b2680-5fd3-4772-b89b-9a0b6b58a41c"),
                        level = "1A",
                    ),
                    office = OsmsOffice(
                        officeUid = OsmsOfficeUid("497c568c-8e6b-45ff-9b94-4840c5d05e2b"),
                        name = "Марс"
                    ),
                    startTime = "2023-12-21T10:00:00.000",
                    endTime = "2023-12-21T16:00:00.000",
                )
            ),
            state = OsmsState.RUNNING,
        )

        @JvmStatic
        private fun listOfContexts() = listOf(
            createContext,
            readContext,
            updateContext,
            deleteContext,
            searchContext,
        )
    }
}