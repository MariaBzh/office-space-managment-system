package ru.otus.osms.ktor.test.common.stub

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import ru.otus.osms.api.v1.kpm.apiV1Mapper
import ru.otus.osms.api.v1.kpm.models.*
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.ktor.module
import ru.otus.osms.repo.stubs.BookingRepoStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class V1BookingStubApiTest {
    @Test
    fun create() = v1TestApplication { client ->
        val requestObj = BookingCreateRequest(
            requestUid = "qwerty123",
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
                startTime = "2024-01-01T10:00:00.0000",
                endTime = "2024-01-01T11:00:00.0000",
            ),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS
            )
        )

        val response = client.post("api/common/v1/bookings/create") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingCreateResponse

        assertEquals(200, response.status.value)
        assertEquals("booking-3000", responseObj.booking?.bookingUid)
    }


    @Test
    fun read() = v1TestApplication { client ->
        val requestObj = BookingReadRequest(
            requestUid = "qwerty123",
            booking = BookingReadObject("booking-3000"),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS
            )
        )

        val response = client.post("api/common/v1/bookings/read") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingReadResponse

        assertEquals(200, response.status.value)
        assertEquals(requestObj.booking?.bookingUid, responseObj.booking?.bookingUid)
    }

    @Test
    fun update() = v1TestApplication { client ->
        val requestObj = BookingUpdateRequest(
            requestUid = "qwerty123",
            booking = BookingUpdateObject(
                bookingUid = "booking-3000",
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
                startTime = "2024-01-01T10:00:00.0000",
                endTime = "2024-01-01T11:00:00.0000",
            ),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS
            )
        )

        val response = client.post("api/common/v1/bookings/update") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingUpdateResponse

        assertEquals(200, response.status.value)
        assertEquals(requestObj.booking?.bookingUid, responseObj.booking?.bookingUid)
        assertEquals(requestObj.booking?.startTime, responseObj.booking?.startTime)
        assertEquals(requestObj.booking?.endTime, responseObj.booking?.endTime)
    }

    @Test
    fun delete() = v1TestApplication { client ->
        val requestObj = BookingDeleteRequest(
            requestUid = "qwerty123",
            booking = BookingDeleteObject(
                bookingUid = "booking-3000",
            ),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS
            )
        )

        val response = client.post("api/common/v1/bookings/delete") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingDeleteResponse

        assertEquals(200, response.status.value)
        assertEquals(requestObj.booking?.bookingUid, responseObj.booking?.bookingUid)
    }

    @Test
    fun search() = v1TestApplication { client ->
        val requestObj = BookingSearchRequest(
            requestUid = "qwerty123",
            bookingFilter = BookingSearchFilter(),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS
            )
        )

        val response = client.post("api/common/v1/bookings/search") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingSearchResponse

        assertEquals(200, response.status.value)
        assertEquals(1, responseObj.bookings?.size)
    }

    @Test
    fun notFound() = v1TestApplication { client ->
        val requestObj = BookingReadRequest(
            requestUid = "qwerty123",
            booking = BookingReadObject("booking-3001"),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.NOT_FOUND
            )
        )

        val response = client.post("api/common/v1/bookings/read") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingReadResponse

        assertEquals(200, response.status.value)
        assertNotNull(responseObj.errors)
        assertEquals(requestObj.debug?.stub?.name?.toLowerCasePreservingASCIIRules(), responseObj.errors?.first()?.code)
    }

    @Test
    fun badUid() = v1TestApplication { client ->
        val requestObj = BookingReadRequest(
            requestUid = "qwerty123",
            booking = BookingReadObject("booking-3001#$%^&"),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.BAD_UID
            )
        )

        val response = client.post("api/common/v1/bookings/read") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingReadResponse

        assertEquals(200, response.status.value)
        assertNotNull(responseObj.errors)
        assertNotNull(responseObj.errors?.first()?.field)
        assertEquals(requestObj.debug?.stub?.name?.toLowerCasePreservingASCIIRules(), responseObj.errors?.first()?.code)
    }

    @Test
    fun badTime() = v1TestApplication { client ->
        val requestObj = BookingCreateRequest(
            requestUid = "qwerty123",
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
                startTime = "2024-01-01T13:00:00.0000",
                endTime = "2024-01-01T11:00:00.0000",
            ),
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.BAD_TIME
            )
        )

        val response = client.post("api/common/v1/bookings/create") {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingCreateResponse

        assertEquals(200, response.status.value)
        assertNotNull(responseObj.errors)
        assertNotNull(responseObj.errors?.first()?.field)
        assertEquals(requestObj.debug?.stub?.name?.toLowerCasePreservingASCIIRules(), responseObj.errors?.first()?.code)
    }

    private fun v1TestApplication(function: suspend (HttpClient) -> Unit): Unit = testApplication {
        application { module(OsmsAppSettings(corSettings = OsmsCorSettings(repoStub = BookingRepoStub()))) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }
        }
        function(client)
    }
}
