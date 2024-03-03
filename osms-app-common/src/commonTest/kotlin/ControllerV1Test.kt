package ru.osms.app.common.test

import kotlinx.coroutines.test.runTest
import ru.osms.app.common.IOsmsAppSettings
import ru.osms.app.common.controllerHelper
import ru.otus.osms.api.v1.kpm.models.*
import ru.otus.osms.biz.OsmsBookingProcessor
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.mappers.kmp.v1.fromTransport
import ru.otus.osms.mappers.kmp.v1.toTransportBooking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ControllerV1Test {

    class TestApplicationCall(val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.testBookingKtor(appSettings: IOsmsAppSettings) {
        val resp = appSettings.controllerHelper(
            {
                when (request) {
                    is BookingCreateRequest -> fromTransport(receive<BookingCreateRequest>())
                    is BookingReadRequest -> fromTransport(receive<BookingReadRequest>())
                    is BookingUpdateRequest -> fromTransport(receive<BookingUpdateRequest>())
                    is BookingDeleteRequest -> fromTransport(receive<BookingDeleteRequest>())
                    is BookingSearchRequest -> fromTransport(receive<BookingSearchRequest>())
                }
            },
            {
                when (request) {
                    is BookingCreateRequest -> toTransportBooking() as BookingCreateResponse
                    is BookingReadRequest -> toTransportBooking() as BookingReadResponse
                    is BookingUpdateRequest -> toTransportBooking() as BookingUpdateResponse
                    is BookingDeleteRequest -> toTransportBooking() as BookingDeleteResponse
                    is BookingSearchRequest -> toTransportBooking() as BookingSearchResponse
                }
            },
            ControllerV1Test::class,
            "controller-v1-test"
        )
        respond(resp)
    }

    @Test
    fun ktorHelperSuccessTest() = runTest {
        successfulRequests.forEach {
            val testApp = TestApplicationCall(it).apply { testBookingKtor(appSettings) }
            val res = testApp.res

            assertNotNull(res)

            assertEquals(ResponseResult.SUCCESS, res.result)

            assertNull(res.errors)

            when (it) {
                is BookingCreateRequest -> assertEquals(createRequest.requestUid, res.requestUid)
                is BookingReadRequest -> assertEquals(readRequest.requestUid, res.requestUid)
                is BookingUpdateRequest -> assertEquals(updateRequest.requestUid, res.requestUid)
                is BookingDeleteRequest -> assertEquals(deleteRequest.requestUid, res.requestUid)
                is BookingSearchRequest -> assertEquals(searchRequest.requestUid, res.requestUid)
            }
        }
    }

    @Test
    fun ktorHelperFailedTest() = runTest {
        failedRequests.forEach {
            val testApp = TestApplicationCall(it).apply { testBookingKtor(appSettings) }
            val res = testApp.res

            assertNotNull(res)
            assertNotNull(res.errors)

            assertEquals(ResponseResult.ERROR, res.result)

            when (it) {
                is BookingCreateRequest -> assertEquals(badTimeCreateRequest.requestUid, res.requestUid)
                is BookingReadRequest -> assertEquals(notFoundReadRequest.requestUid, res.requestUid)
                is BookingSearchRequest -> assertEquals(badUidSearchRequest.requestUid, res.requestUid)
                else -> {}
            }
        }
    }

    companion object {
        private val createRequest = BookingCreateRequest(
            requestUid = "d945bsaf-rt10-d5ee-ff40-12qw45d113op",
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingCreateObject(
                userUid = "f947b0f7-5df1-4580-8848-88e245b219a2",
                branch = Branch(
                    branchUid = "2cc01441-4d6e-48de-a56d-cacc0e32f5a9",
                    name = "Московский",
                ),
                floor = Floor(
                    floorUid = "542b2680-5fd3-4772-b89b-9a0b6b58a41c",
                    level = "1A",
                ),
                office = Office(
                    officeUid = "497c568c-8e6b-45ff-9b94-4840c5d05e2b",
                    name = "Марс"
                ),
                workspaceUid = "3d6acb16-6e60-42ca-8fd3-a7492fa21caa",
                startTime = "2023-12-21 10:00:00.000",
                endTime = "2023-12-21 16:00:00.000",
            )
        )

        private val readRequest = BookingReadRequest(
            requestUid = "7fdf1ab6-e72b-41d3-ae77-6f7a1f518ac3",
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingReadObject(
                bookingUid = "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
            )
        )

        private val updateRequest = BookingUpdateRequest(
            requestUid = "19be0398-cad0-4b87-bde5-179ffa0bb3d6",
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingUpdateObject(
                bookingUid = "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
                userUid = "f947b0f7-5df1-4580-8848-88e245b219a2",
                branch = Branch(
                    branchUid = "2cc01441-4d6e-48de-a56d-cacc0e32f5a9",
                    name = "Московский",
                ),
                floor = Floor(
                    floorUid = "542b2680-5fd3-4772-b89b-9a0b6b58a41c",
                    level = "1A",
                ),
                office = Office(
                    officeUid = "497c568c-8e6b-45ff-9b94-4840c5d05e2b",
                    name = "Марс"
                ),
                workspaceUid = "3d6acb16-6e60-42ca-8fd3-a7492fa21caa",
                startTime = "2023-12-21 10:00:00.000",
                endTime = "2023-12-21 16:00:00.000",
            )
        )

        private val deleteRequest = BookingDeleteRequest(
            requestUid = "f1313810-5f80-48ff-a4e8-80ffca1b36e6",
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingDeleteObject(
                bookingUid = "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
            )
        )

        private val searchRequest = BookingSearchRequest(
            requestUid = "f0779de9-c70f-4c9f-a91a-69e5dee6d8fd",
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            bookingFilter = BookingSearchFilter(
                userUid = "f947b0f7-5df1-4580-8848-88e245b219a2",
                branch = Branch(
                    branchUid = "2cc01441-4d6e-48de-a56d-cacc0e32f5a9",
                    name = "Московский",
                ),
                floor = Floor(
                    floorUid = "542b2680-5fd3-4772-b89b-9a0b6b58a41c",
                    level = "1A",
                ),
                office = Office(
                    officeUid = "497c568c-8e6b-45ff-9b94-4840c5d05e2b",
                    name = "Марс"
                ),
                workspaceUid = "3d6acb16-6e60-42ca-8fd3-a7492fa21caa",
                startTime = "2023-12-21 10:00:00.000",
                endTime = "2023-12-21 16:00:00.000",
            )
        )

        private val badTimeCreateRequest = BookingCreateRequest(
            requestUid = "bad-time",
            booking = BookingCreateObject(
                userUid = "user-1",
                branch = Branch(
                    branchUid = "branch-1",
                    name = "A"
                ),
                floor = Floor(
                    floorUid = "floor-1",
                    level = "1"
                ),
                office = Office(
                    officeUid = "office-1",
                    name = "Московский"
                ),
                workspaceUid = "workspace-1",
                startTime = "2024-01-01 13:00:00.0000",
                endTime = "2024-01-01 11:00:00.0000",
            ),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.BAD_TIME
            )
        )

        private val notFoundReadRequest = BookingReadRequest(
            requestUid = "not-found",
            booking = BookingReadObject("booking-3001"),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.NOT_FOUND
            )
        )

        private val badUidSearchRequest = BookingSearchRequest(
            requestUid = "bad-uid",
            bookingFilter = BookingSearchFilter(
                userUid = "booking-3001#$%^&",
            ),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.BAD_UID
            )
        )

        private val failedRequests = setOf(
            badTimeCreateRequest,
            notFoundReadRequest,
            badUidSearchRequest,
        )

        private val successfulRequests = setOf(
            createRequest,
            readRequest,
            updateRequest,
            deleteRequest,
            searchRequest,
        )

        private val appSettings: IOsmsAppSettings = object : IOsmsAppSettings {
            override val processor: OsmsBookingProcessor = OsmsBookingProcessor()
            override val corSettings: OsmsCorSettings = OsmsCorSettings()
        }
    }
}
