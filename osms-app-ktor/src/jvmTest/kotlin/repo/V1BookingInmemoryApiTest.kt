package ru.otus.osms.ktor.test.repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import ru.otus.osms.api.v1.models.*
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.IBookingRepository
import ru.otus.osms.db.inmemory.BookingRepoInMemory
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.ktor.jvm.moduleJvm
import ru.otus.osms.stubs.OsmsBookingStub

class V1BookingInmemoryApiTest {
    @Test
    fun create() = v1TestApplication(BookingRepoInMemory(randomUuid = { UUID_NEW })) { client ->
        val createBooking = BookingCreateObject(
            userUid = "user-1",
            workspaceUid = "workspace-1",
            branch = Branch("branch-1", "Branch"),
            floor = Floor("floor-1", "1", "Floor"),
            office = Office("office-1", "Office"),
            description = "Test",
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
        )

        val response = client.post(CREATE_URI) {
            val requestObj = BookingCreateRequest(
                requestUid = "request-1",
                booking = createBooking,
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.TEST,
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseText = response.bodyAsText()
        val responseObj = response.body<IResponse>() as BookingCreateResponse

        println(responseText)

        assertEquals(200, response.status.value)
        assertEquals(UUID_NEW, responseObj.booking?.bookingUid)
        assertEquals(createBooking.userUid, responseObj.booking?.userUid)
        assertEquals(createBooking.workspaceUid, responseObj.booking?.workspaceUid)
        assertEquals(createBooking.branch?.branchUid, responseObj.booking?.branch?.branchUid)
        assertEquals(createBooking.branch?.name, responseObj.booking?.branch?.name)
        assertEquals(createBooking.floor?.floorUid, responseObj.booking?.floor?.floorUid)
        assertEquals(createBooking.floor?.level, responseObj.booking?.floor?.level)
        assertEquals(createBooking.office?.officeUid, responseObj.booking?.office?.officeUid)
        assertEquals(createBooking.office?.name, responseObj.booking?.office?.name)
        assertEquals(createBooking.description, responseObj.booking?.description)
        assertEquals(createBooking.startTime, responseObj.booking?.startTime)
        assertEquals(createBooking.endTime, responseObj.booking?.endTime)
    }

    @Test
    fun read() = v1TestApplication(BookingRepoInMemory(initObjects = listOf(initBooking), randomUuid = { UUID_NEW })) { client ->
        val response = client.post(READ_URI) {
            val requestObj = BookingReadRequest(
                requestUid = "request-1",
                booking = BookingReadObject(
                    bookingUid = UUID_OLD
                ),
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.TEST,
                ),
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingReadResponse

        assertEquals(200, response.status.value)
        assertEquals(
            initBooking.bookingUid.asString(),
            responseObj.booking?.bookingUid
        )
        assertEquals(initBooking.userUid.asString(), responseObj.booking?.userUid)
        assertEquals(
            initBooking.workspaceUid.asString(),
            responseObj.booking?.workspaceUid
        )
        assertEquals(
            initBooking.branch.branchUid.asString(),
            responseObj.booking?.branch?.branchUid
        )
        assertEquals(initBooking.branch.name, responseObj.booking?.branch?.name)
        assertEquals(
            initBooking.floor.floorUid.asString(),
            responseObj.booking?.floor?.floorUid
        )
        assertEquals(initBooking.floor.level, responseObj.booking?.floor?.level)
        assertEquals(
            initBooking.office.officeUid.asString(),
            responseObj.booking?.office?.officeUid
        )
        assertEquals(initBooking.office.name, responseObj.booking?.office?.name)
        assertEquals(initBooking.description, responseObj.booking?.description)
        assertEquals(initBooking.startTime, responseObj.booking?.startTime)
        assertEquals(initBooking.endTime, responseObj.booking?.endTime)
    }

    @Test
    fun update() = v1TestApplication(BookingRepoInMemory(initObjects = listOf(initBooking), randomUuid = { UUID_NEW })) { client ->
        val bookingUpdate = BookingUpdateObject(
            bookingUid = UUID_OLD,
            userUid = "user-1",
            workspaceUid = "workspace-1",
            branch = Branch("branch-1", "Branch"),
            floor = Floor("floor-1", "1", "Floor"),
            office = Office("office-1", "Office"),
            description = "Test",
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
            lock = initBooking.lock.asString(),
        )

        val response = client.post(UPDATE_URI) {
            val requestObj = BookingUpdateRequest(
                requestUid = "request-1",
                booking = bookingUpdate
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingUpdateResponse

        assertEquals(200, response.status.value)
        assertEquals(
            initBooking.bookingUid.asString(),
            responseObj.booking?.bookingUid
        )
        assertEquals(initBooking.userUid.asString(), responseObj.booking?.userUid)
        assertEquals(
            initBooking.workspaceUid.asString(),
            responseObj.booking?.workspaceUid
        )
        assertEquals(
            initBooking.branch.branchUid.asString(),
            responseObj.booking?.branch?.branchUid
        )
        assertEquals(initBooking.branch.name, responseObj.booking?.branch?.name)
        assertEquals(
            initBooking.floor.floorUid.asString(),
            responseObj.booking?.floor?.floorUid
        )
        assertEquals(initBooking.floor.level, responseObj.booking?.floor?.level)
        assertEquals(
            initBooking.office.officeUid.asString(),
            responseObj.booking?.office?.officeUid
        )
        assertEquals(initBooking.office.name, responseObj.booking?.office?.name)
        assertEquals("Test", responseObj.booking?.description)
        assertEquals("2024-01-01T10:00:00", responseObj.booking?.startTime)
        assertEquals("2024-01-01T12:00:00", responseObj.booking?.endTime)
    }

    @Test
    fun delete() = v1TestApplication(BookingRepoInMemory(initObjects = listOf(initBooking), randomUuid = { UUID_NEW })) { client ->
        val response = client.post(DELETE_URI) {
            val requestObj = BookingDeleteRequest(
                requestUid = "request-1",
                booking = BookingDeleteObject(
                    bookingUid = UUID_OLD,
                    lock = initBooking.lock.asString(),
                ),
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.TEST,
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingDeleteResponse

        assertEquals(200, response.status.value)
        assertEquals(
            initBooking.bookingUid.asString(),
            responseObj.booking?.bookingUid
        )
        assertEquals(initBooking.userUid.asString(), responseObj.booking?.userUid)
        assertEquals(
            initBooking.workspaceUid.asString(),
            responseObj.booking?.workspaceUid
        )
        assertEquals(
            initBooking.branch.branchUid.asString(),
            responseObj.booking?.branch?.branchUid
        )
        assertEquals(initBooking.branch.name, responseObj.booking?.branch?.name)
        assertEquals(
            initBooking.floor.floorUid.asString(),
            responseObj.booking?.floor?.floorUid
        )
        assertEquals(initBooking.floor.level, responseObj.booking?.floor?.level)
        assertEquals(
            initBooking.office.officeUid.asString(),
            responseObj.booking?.office?.officeUid
        )
        assertEquals(initBooking.office.name, responseObj.booking?.office?.name)
        assertEquals(initBooking.description, responseObj.booking?.description)
        assertEquals(initBooking.startTime, responseObj.booking?.startTime)
        assertEquals(initBooking.endTime, responseObj.booking?.endTime)
    }

    @Test
    fun search() = v1TestApplication(BookingRepoInMemory(initObjects = listOf(initBooking), randomUuid = { UUID_NEW })) { client ->
        val response = client.post(SEARCH_URI) {
            val requestObj = BookingSearchRequest(
                requestUid = "request-1",
                bookingFilter = BookingSearchFilter(
                    userUid = "user-1"
                ),
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.TEST,
                )
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }

        val responseObj = response.body<IResponse>() as BookingSearchResponse

        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.bookings?.size)
    }

    private fun v1TestApplication(repo: IBookingRepository, function: suspend (HttpClient) -> Unit): Unit = testApplication {
        application {
            moduleJvm(OsmsAppSettings(corSettings = OsmsCorSettings(repoTest = repo)))
        }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
    
        function(client)
    }
    
    companion object {
        private const val UUID_OLD = "booking-1"
        private const val UUID_NEW = "booking-2"

        private const val CREATE_URI = "api/v1/bookings/create"
        private const val READ_URI = "api/v1/bookings/read"
        private const val UPDATE_URI = "api/v1/bookings/update"
        private const val DELETE_URI = "api/v1/bookings/delete"
        private const val SEARCH_URI = "api/v1/bookings/search"

        private val initBooking = OsmsBookingStub.prepareResult {
            bookingUid = OsmsBookingUid(UUID_OLD)
            userUid = OsmsUserUid("user-1")
            workspaceUid = OsmsWorkspaceUid("workspace-1")
            branch = OsmsBranch(OsmsBranchUid("branch-1"),"Branch")
            floor = OsmsFloor(OsmsFloorUid("floor-1"), "1","Floor")
            office = OsmsOffice(OsmsOfficeUid("office-1"), "Office")
            description = "Init test model"
            startTime = "2024-01-01T10:00:00.0000"
            endTime = "2024-01-01T12:00:00.0000"
            lock = OsmsBookingLock(UUID_OLD)
        }
    }
}