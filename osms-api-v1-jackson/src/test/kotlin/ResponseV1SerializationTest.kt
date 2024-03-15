package ru.otus.osms.api.v1.jackson.test

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.otus.osms.api.v1.jackson.apiV1Mapper
import ru.otus.osms.api.v1.models.*
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {

    @ParameterizedTest(name = "{index} {0} serialized")
    @MethodSource("listOfResponses")
    fun serialize(response: IResponse) {
        val json = apiV1Mapper.writeValueAsString(response)

        when (response) {
            is BookingCreateResponse -> {
                assertContains(json, Regex("\"responseType\":\\s*\"create\""))

                if (response.errors.isNullOrEmpty()) {
                    assertContains(json, Regex("\"bookingUid\":\\s*\"${response.booking?.bookingUid}\""))

                    assertContains(json, Regex("\"userUid\":\\s*\"${response.booking?.userUid}\""))

                    assertContains(json, Regex("\"branchUid\":\\s*\"${response.booking?.branch?.branchUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${response.booking?.branch?.name}\""))

                    assertContains(json, Regex("\"floorUid\":\\s*\"${response.booking?.floor?.floorUid}\""))
                    assertContains(json, Regex("\"level\":\\s*\"${response.booking?.floor?.level}\""))

                    assertContains(json, Regex("\"officeUid\":\\s*\"${response.booking?.office?.officeUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${response.booking?.office?.name}\""))

                    assertContains(json, Regex("\"workspaceUid\":\\s*\"${response.booking?.workspaceUid}\""))
                    assertContains(json, Regex("\"startTime\":\\s*\"${response.booking?.startTime}\""))
                    assertContains(json, Regex("\"endTime\":\\s*\"${response.booking?.endTime}\""))
                } else {
                    assertContains(json, Regex("\"errors\":\\s*"))

                    response.errors?.forEach {
                        assertContains(json, Regex("\"code\":\\s*\"${it.code}\""))
                        assertContains(json, Regex("\"group\":\\s*\"${it.group}\""))
                        assertContains(json, Regex("\"message\":\\s*\"${it.message}\""))
                    }
                }
            }
            is  BookingReadResponse -> {
                assertContains(json, Regex("\"responseType\":\\s*\"read\""))

                assertContains(json, Regex("\"bookingUid\":\\s*\"${response.booking?.bookingUid}\""))

                assertContains(json, Regex("\"userUid\":\\s*\"${response.booking?.userUid}\""))

                assertContains(json, Regex("\"branchUid\":\\s*\"${response.booking?.branch?.branchUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${response.booking?.branch?.name}\""))

                assertContains(json, Regex("\"floorUid\":\\s*\"${response.booking?.floor?.floorUid}\""))
                assertContains(json, Regex("\"level\":\\s*\"${response.booking?.floor?.level}\""))

                assertContains(json, Regex("\"officeUid\":\\s*\"${response.booking?.office?.officeUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${response.booking?.office?.name}\""))

                assertContains(json, Regex("\"workspaceUid\":\\s*\"${response.booking?.workspaceUid}\""))
                assertContains(json, Regex("\"startTime\":\\s*\"${response.booking?.startTime}\""))
                assertContains(json, Regex("\"endTime\":\\s*\"${response.booking?.endTime}\""))
            }
            is BookingUpdateResponse -> {
                assertContains(json, Regex("\"responseType\":\\s*\"update\""))

                assertContains(json, Regex("\"bookingUid\":\\s*\"${response.booking?.bookingUid}\""))

                assertContains(json, Regex("\"userUid\":\\s*\"${response.booking?.userUid}\""))

                assertContains(json, Regex("\"branchUid\":\\s*\"${response.booking?.branch?.branchUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${response.booking?.branch?.name}\""))

                assertContains(json, Regex("\"floorUid\":\\s*\"${response.booking?.floor?.floorUid}\""))
                assertContains(json, Regex("\"level\":\\s*\"${response.booking?.floor?.level}\""))

                assertContains(json, Regex("\"officeUid\":\\s*\"${response.booking?.office?.officeUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${response.booking?.office?.name}\""))

                assertContains(json, Regex("\"workspaceUid\":\\s*\"${response.booking?.workspaceUid}\""))
                assertContains(json, Regex("\"startTime\":\\s*\"${response.booking?.startTime}\""))
                assertContains(json, Regex("\"endTime\":\\s*\"${response.booking?.endTime}\""))
            }
            is BookingDeleteResponse -> {
                assertContains(json, Regex("\"responseType\":\\s*\"delete\""))

                assertContains(json, Regex("\"bookingUid\":\\s*\"${response.booking?.bookingUid}\""))

                assertContains(json, Regex("\"userUid\":\\s*\"${response.booking?.userUid}\""))

                assertContains(json, Regex("\"branchUid\":\\s*\"${response.booking?.branch?.branchUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${response.booking?.branch?.name}\""))

                assertContains(json, Regex("\"floorUid\":\\s*\"${response.booking?.floor?.floorUid}\""))
                assertContains(json, Regex("\"level\":\\s*\"${response.booking?.floor?.level}\""))

                assertContains(json, Regex("\"officeUid\":\\s*\"${response.booking?.office?.officeUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${response.booking?.office?.name}\""))

                assertContains(json, Regex("\"workspaceUid\":\\s*\"${response.booking?.workspaceUid}\""))
                assertContains(json, Regex("\"startTime\":\\s*\"${response.booking?.startTime}\""))
                assertContains(json, Regex("\"endTime\":\\s*\"${response.booking?.endTime}\""))
            }
            is BookingSearchResponse -> {
                assertContains(json, Regex("\"responseType\":\\s*\"search\""))

                assertContains(json, Regex("\"bookings\":\\s*"))
            }
        }


    }

    @ParameterizedTest(name = "{index} {0} deserialized")
    @MethodSource("listOfResponses")
    fun deserialize(response: IResponse) {
        val json = apiV1Mapper.writeValueAsString(response)

        when (val obj = apiV1Mapper.readValue(json, IResponse::class.java)) {
            is BookingCreateResponse,
            is BookingReadResponse,
            is BookingUpdateResponse,
            is BookingDeleteResponse,
            is BookingSearchResponse -> {
                assertEquals(response, obj)
            }
        }

    }

    companion object {
        private val createResponse = BookingCreateResponse(
            requestUid = UUID.randomUUID().toString(),
            booking = BookingResponseObject(
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
                permissions = setOf(
                    BookingPermissions.READ,
                    BookingPermissions.UPDATE,
                    BookingPermissions.DELETE,
                )
            )
        )

        private val createErrorResponse = BookingCreateResponse(
            requestUid = UUID.randomUUID().toString(),
            errors = listOf(
                Error(
                    code = "404",
                    group = "Object",
                    message = "Not found",
                )
            )
        )

        @JvmStatic
        private fun listOfResponses() = listOf(
            createResponse,
            createErrorResponse,
        )
    }
}