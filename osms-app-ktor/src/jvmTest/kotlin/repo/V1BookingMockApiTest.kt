package ru.otus.osms.ktor.test.repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import ru.otus.osms.api.v1.models.*
import ru.otus.osms.common.OsmsCorSettings
import ru.otus.osms.common.models.*
import ru.otus.osms.common.repo.DbBookingResponse
import ru.otus.osms.common.repo.DbBookingsResponse
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.ktor.jvm.moduleJvm
import ru.otus.osms.repo.test.BookingRepositoryMock

class V1BookingMockApiTest {
    @Test
    fun create() = testApplication {
        val repo = BookingRepositoryMock(
            invokeCreateBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = it.booking.copy(bookingUid = OsmsBookingUid(UUID)),
                )
            }
        )
        application {
            moduleJvm(OsmsAppSettings(corSettings = OsmsCorSettings(repoTest = repo)))
        }
        val client = myClient()
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
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as BookingCreateResponse
        
        assertEquals(200, response.status.value)
        assertEquals(UUID, responseObj.booking?.bookingUid)
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
    fun read() = testApplication {
        val repo = BookingRepositoryMock(
            invokeReadBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = INIT_DATA,
                )
            }
        )
        application {
            moduleJvm(OsmsAppSettings(corSettings = OsmsCorSettings(repoTest = repo)))
        }
        val client = myClient()
        val response = client.post(READ_URI) {
            val requestObj = BookingReadRequest(
                requestUid = "request-1",
                booking = BookingReadObject(
                    bookingUid = UUID
                ),
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as BookingReadResponse
        
        assertEquals(200, response.status.value)
        assertEquals(UUID, responseObj.booking?.bookingUid)
        assertEquals(INIT_DATA.userUid.asString(), responseObj.booking?.userUid)
        assertEquals(INIT_DATA.workspaceUid.asString(), responseObj.booking?.workspaceUid)
        assertEquals(INIT_DATA.branch.branchUid.asString(), responseObj.booking?.branch?.branchUid)
        assertEquals(INIT_DATA.branch.name, responseObj.booking?.branch?.name)
        assertEquals(INIT_DATA.floor.floorUid.asString(), responseObj.booking?.floor?.floorUid)
        assertEquals(INIT_DATA.floor.level, responseObj.booking?.floor?.level)
        assertEquals(INIT_DATA.office.officeUid.asString(), responseObj.booking?.office?.officeUid)
        assertEquals(INIT_DATA.office.name, responseObj.booking?.office?.name)
        assertEquals(INIT_DATA.description, responseObj.booking?.description)
        assertEquals(INIT_DATA.startTime, responseObj.booking?.startTime)
        assertEquals(INIT_DATA.endTime, responseObj.booking?.endTime)
    }

    @Test
    fun update() = testApplication {
        val repo = BookingRepositoryMock(
            invokeReadBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = INIT_DATA,
                )
            },
            invokeUpdateBooking = {
                DbBookingResponse(
                    isSuccess = true,
                    data = it.booking.copy(endTime = "2024-01-01T13:00:00"),
                )
            }
        )
        application {
            moduleJvm(OsmsAppSettings(corSettings = OsmsCorSettings(repoTest = repo)))
        }
        val client = myClient()
        val bookingUpdate = BookingUpdateObject(
            bookingUid = UUID,
            userUid = "user-1",
            workspaceUid = "workspace-1",
            branch = Branch("branch-1", "Branch"),
            floor = Floor("floor-1", "1", "Floor"),
            office = Office("office-1", "Office"),
            description = "Test",
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T13:00:00",
            lock = "123"
        )
        val response = client.post(UPDATE_URI) {
            val requestObj = BookingUpdateRequest(
                requestUid = "request-1",
                booking = bookingUpdate,
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as BookingUpdateResponse

        assertEquals(200, response.status.value)
        assertEquals(UUID, responseObj.booking?.bookingUid)
        assertEquals(INIT_DATA.userUid.asString(), responseObj.booking?.userUid)
        assertEquals(INIT_DATA.workspaceUid.asString(), responseObj.booking?.workspaceUid)
        assertEquals(INIT_DATA.branch.branchUid.asString(), responseObj.booking?.branch?.branchUid)
        assertEquals(INIT_DATA.branch.name, responseObj.booking?.branch?.name)
        assertEquals(INIT_DATA.floor.floorUid.asString(), responseObj.booking?.floor?.floorUid)
        assertEquals(INIT_DATA.floor.level, responseObj.booking?.floor?.level)
        assertEquals(INIT_DATA.office.officeUid.asString(), responseObj.booking?.office?.officeUid)
        assertEquals(INIT_DATA.office.name, responseObj.booking?.office?.name)
        assertEquals(INIT_DATA.description, responseObj.booking?.description)
        assertEquals(INIT_DATA.startTime, responseObj.booking?.startTime)
        assertEquals("2024-01-01T13:00:00", responseObj.booking?.endTime)
    }

    @Test
    fun delete() = testApplication {
        application {
            val repo = BookingRepositoryMock(
                invokeReadBooking = {
                    DbBookingResponse(
                        isSuccess = true,
                        data = INIT_DATA,
                    )
                },
                invokeDeleteBooking = {
                    DbBookingResponse(
                        isSuccess = true,
                        data = INIT_DATA,
                    )
                }
            )
            moduleJvm(OsmsAppSettings(corSettings = OsmsCorSettings(repoTest = repo)))
        }
        val client = myClient()
        val deleteUid = UUID
        val response = client.post(DELETE_URI) {
            val requestObj = BookingDeleteRequest(
                requestUid = "request-1",
                booking = BookingDeleteObject(
                    bookingUid = deleteUid,
                    lock = "123",
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
        assertEquals(UUID, responseObj.booking?.bookingUid)
        assertEquals(INIT_DATA.userUid.asString(), responseObj.booking?.userUid)
        assertEquals(INIT_DATA.workspaceUid.asString(), responseObj.booking?.workspaceUid)
        assertEquals(INIT_DATA.branch.branchUid.asString(), responseObj.booking?.branch?.branchUid)
        assertEquals(INIT_DATA.branch.name, responseObj.booking?.branch?.name)
        assertEquals(INIT_DATA.floor.floorUid.asString(), responseObj.booking?.floor?.floorUid)
        assertEquals(INIT_DATA.floor.level, responseObj.booking?.floor?.level)
        assertEquals(INIT_DATA.office.officeUid.asString(), responseObj.booking?.office?.officeUid)
        assertEquals(INIT_DATA.office.name, responseObj.booking?.office?.name)
        assertEquals(INIT_DATA.description, responseObj.booking?.description)
        assertEquals(INIT_DATA.startTime, responseObj.booking?.startTime)
        assertEquals(INIT_DATA.endTime, responseObj.booking?.endTime)
    }

    @Test
    fun search() = testApplication {
        application {
            val repo =
                BookingRepositoryMock(
                    invokeSearchBooking = {
                        DbBookingsResponse(
                            isSuccess = true,
                            data = listOf(INIT_DATA),
                        )
                    }
                )
            moduleJvm(OsmsAppSettings(corSettings = OsmsCorSettings(repoTest = repo)))
        }
        val client = myClient()
        val response = client.post(SEARCH_URI) {
            val requestObj = BookingSearchRequest(
                requestUid = "request-1",
                bookingFilter = BookingSearchFilter(userUid = "user-1"),
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

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    companion object {
        private const val CREATE_URI = "api/v1/bookings/create"
        private const val READ_URI = "api/v1/bookings/read"
        private const val UPDATE_URI = "api/v1/bookings/update"
        private const val DELETE_URI = "api/v1/bookings/delete"
        private const val SEARCH_URI = "api/v1/bookings/search"

        private const val UUID = "4101d2f9-eb84-445f-8f16-a9fd8ab26daa"

        val INIT_DATA = OsmsBooking(
            bookingUid = OsmsBookingUid(UUID),
            userUid = OsmsUserUid("user-1"),
            workspaceUid = OsmsWorkspaceUid("workspace-1"),
            branch = OsmsBranch(OsmsBranchUid("branch-1"), "Branch"),
            floor = OsmsFloor(OsmsFloorUid("floor-1"), "1", "Floor"),
            office = OsmsOffice(OsmsOfficeUid("office-1"), "Office"),
            description = "Test",
            startTime = "2024-01-01T10:00:00",
            endTime = "2024-01-01T12:00:00",
        )
    }
}