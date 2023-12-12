package ru.otus.osms.api.v1.test

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.otus.osms.api.v1.apiV1Mapper
import ru.otus.osms.api.v1.models.*
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {

    @ParameterizedTest(name = "{index} {0} serialized")
    @MethodSource("listOfRequests")
    fun serialize(request: IRequest) {
        val json = apiV1Mapper.writeValueAsString(request)

        when (request) {
            is BookingCreateRequest -> {
                assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
                assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
                assertContains(json, Regex("\"requestType\":\\s*\"create\""))

                assertContains(json, Regex("\"userUid\":\\s*\"${request.booking?.userUid}\""))

                assertContains(json, Regex("\"branchUid\":\\s*\"${request.booking?.branch?.branchUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${request.booking?.branch?.name}\""))

                assertContains(json, Regex("\"floorUid\":\\s*\"${request.booking?.floor?.floorUid}\""))
                assertContains(json, Regex("\"level\":\\s*\"${request.booking?.floor?.level}\""))

                assertContains(json, Regex("\"officeUid\":\\s*\"${request.booking?.office?.officeUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${request.booking?.office?.name}\""))

                assertContains(json, Regex("\"workspaceUid\":\\s*\"${request.booking?.workspaceUid}\""))
                assertContains(json, Regex("\"startTime\":\\s*\"${request.booking?.startTime}\""))
                assertContains(json, Regex("\"endTime\":\\s*\"${request.booking?.endTime}\""))
            }
            is BookingReadRequest -> {
                assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
                assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
                assertContains(json, Regex("\"requestType\":\\s*\"read\""))

                assertContains(json, Regex("\"bookingUid\":\\s*\"${request.booking?.bookingUid}\""))
            }
            is BookingUpdateRequest -> {
                assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
                assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
                assertContains(json, Regex("\"requestType\":\\s*\"update\""))

                assertContains(json, Regex("\"bookingUid\":\\s*\"${request.booking?.bookingUid}\""))

                assertContains(json, Regex("\"userUid\":\\s*\"${request.booking?.userUid}\""))

                assertContains(json, Regex("\"branchUid\":\\s*\"${request.booking?.branch?.branchUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${request.booking?.branch?.name}\""))

                assertContains(json, Regex("\"floorUid\":\\s*\"${request.booking?.floor?.floorUid}\""))
                assertContains(json, Regex("\"level\":\\s*\"${request.booking?.floor?.level}\""))

                assertContains(json, Regex("\"officeUid\":\\s*\"${request.booking?.office?.officeUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${request.booking?.office?.name}\""))

                assertContains(json, Regex("\"workspaceUid\":\\s*\"${request.booking?.workspaceUid}\""))
                assertContains(json, Regex("\"startTime\":\\s*\"${request.booking?.startTime}\""))
                assertContains(json, Regex("\"endTime\":\\s*\"${request.booking?.endTime}\""))
            }
            is BookingDeleteRequest -> {
                assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
                assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
                assertContains(json, Regex("\"requestType\":\\s*\"delete\""))

                assertContains(json, Regex("\"bookingUid\":\\s*\"${request.booking?.bookingUid}\""))
            }
            is BookingSearchRequest -> {
                assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
                assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
                assertContains(json, Regex("\"requestType\":\\s*\"search\""))

                assertContains(json, Regex("\"userUid\":\\s*\"${request.bookingFilter?.userUid}\""))

                assertContains(json, Regex("\"branchUid\":\\s*\"${request.bookingFilter?.branch?.branchUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${request.bookingFilter?.branch?.name}\""))

                assertContains(json, Regex("\"floorUid\":\\s*\"${request.bookingFilter?.floor?.floorUid}\""))
                assertContains(json, Regex("\"level\":\\s*\"${request.bookingFilter?.floor?.level}\""))

                assertContains(json, Regex("\"officeUid\":\\s*\"${request.bookingFilter?.office?.officeUid}\""))
                assertContains(json, Regex("\"name\":\\s*\"${request.bookingFilter?.office?.name}\""))

                assertContains(json, Regex("\"workspaceUid\":\\s*\"${request.bookingFilter?.workspaceUid}\""))
                assertContains(json, Regex("\"startTime\":\\s*\"${request.bookingFilter?.startTime}\""))
                assertContains(json, Regex("\"endTime\":\\s*\"${request.bookingFilter?.endTime}\""))
            }
        }
    }

    @ParameterizedTest(name = "{index} {0} deserialized")
    @MethodSource("listOfRequests")
    fun deserialize(request: IRequest) {
        val json = apiV1Mapper.writeValueAsString(request)

        when (val obj = apiV1Mapper.readValue(json, IRequest::class.java)) {
            is BookingCreateRequest,
            is BookingReadRequest,
            is BookingUpdateRequest,
            is BookingDeleteRequest,
            is BookingSearchRequest -> {
                assertEquals(request, obj)
            }
        }
    }

    @ParameterizedTest(name = "{index} {0} naked deserialized")
    @MethodSource("listOfJsonStrings")
    fun deserializeNaked(jsonPair: Pair<String, Class<IRequest>>) {
        when (val obj = apiV1Mapper.readValue(jsonPair.first, jsonPair.second)) {
            is BookingCreateRequest -> {
                assertEquals("create", obj.requestType)

                assertEquals("f947b0f7-5df1-4580-8848-88e245b219a2", obj.booking?.userUid)

                assertEquals("2cc01441-4d6e-48de-a56d-cacc0e32f5a9", obj.booking?.branch?.branchUid)
                assertEquals("Московский", obj.booking?.branch?.name)

                assertEquals("542b2680-5fd3-4772-b89b-9a0b6b58a41c", obj.booking?.floor?.floorUid)
                assertEquals("1A", obj.booking?.floor?.level)

                assertEquals("497c568c-8e6b-45ff-9b94-4840c5d05e2b", obj.booking?.office?.officeUid)
                assertEquals("Марс", obj.booking?.office?.name)

                assertEquals("3d6acb16-6e60-42ca-8fd3-a7492fa21caa", obj.booking?.workspaceUid)
                assertEquals("2023-12-21 10:00:00.000", obj.booking?.startTime)
                assertEquals("2023-12-21 16:00:00.000", obj.booking?.endTime)
            }
            is BookingReadRequest -> {
                assertEquals("read", obj.requestType)

                assertEquals("d425f0a6-1hf1-3d10-99e8-25e1r41e27c2", obj.booking?.bookingUid)
            }
            is BookingUpdateRequest -> {
                assertEquals("update", obj.requestType)

                assertEquals("d425f0a6-1hf1-3d10-99e8-25e1r41e27c2", obj.booking?.bookingUid)

                assertEquals("f947b0f7-5df1-4580-8848-88e245b219a2", obj.booking?.userUid)

                assertEquals("2cc01441-4d6e-48de-a56d-cacc0e32f5a9", obj.booking?.branch?.branchUid)
                assertEquals("Московский", obj.booking?.branch?.name)

                assertEquals("542b2680-5fd3-4772-b89b-9a0b6b58a41c", obj.booking?.floor?.floorUid)
                assertEquals("1A", obj.booking?.floor?.level)

                assertEquals("497c568c-8e6b-45ff-9b94-4840c5d05e2b", obj.booking?.office?.officeUid)
                assertEquals("Марс", obj.booking?.office?.name)

                assertEquals("3d6acb16-6e60-42ca-8fd3-a7492fa21caa", obj.booking?.workspaceUid)
                assertEquals("2023-12-21 10:00:00.000", obj.booking?.startTime)
                assertEquals("2023-12-21 16:00:00.000", obj.booking?.endTime)
            }
            is BookingDeleteRequest -> {
                assertEquals("delete", obj.requestType)

                assertEquals("d425f0a6-1hf1-3d10-99e8-25e1r41e27c2", obj.booking?.bookingUid)
            }
            is BookingSearchRequest -> {
                assertEquals("search", obj.requestType)

                assertEquals("f947b0f7-5df1-4580-8848-88e245b219a2", obj.bookingFilter?.userUid)

                assertEquals("2cc01441-4d6e-48de-a56d-cacc0e32f5a9", obj.bookingFilter?.branch?.branchUid)
                assertEquals("Московский", obj.bookingFilter?.branch?.name)

                assertEquals("542b2680-5fd3-4772-b89b-9a0b6b58a41c", obj.bookingFilter?.floor?.floorUid)
                assertEquals("1A", obj.bookingFilter?.floor?.level)

                assertEquals("497c568c-8e6b-45ff-9b94-4840c5d05e2b", obj.bookingFilter?.office?.officeUid)
                assertEquals("Марс", obj.bookingFilter?.office?.name)

                assertEquals("3d6acb16-6e60-42ca-8fd3-a7492fa21caa", obj.bookingFilter?.workspaceUid)
                assertEquals("2023-12-21 10:00:00.000", obj.bookingFilter?.startTime)
                assertEquals("2023-12-21 16:00:00.000", obj.bookingFilter?.endTime)
            }
        }
    }

    companion object {
        private val CREATE_JSON_STRING = """
            {
                "requestType": "create",
                "requestUid": null,
                "debug": {
                        "mode": "stub",
                        "stub": "success"
                },
                "booking": {
                    "userUid": "f947b0f7-5df1-4580-8848-88e245b219a2",
                    "branch": {
                        "branchUid": "2cc01441-4d6e-48de-a56d-cacc0e32f5a9",
                        "name": "Московский",
                        "description": null
                    },
                    "floor": {
                        "floorUid": "542b2680-5fd3-4772-b89b-9a0b6b58a41c",
                        "level": "1A",
                        "description": null
                    },
                    "office": {
                        "officeUid": "497c568c-8e6b-45ff-9b94-4840c5d05e2b",
                        "name": "Марс",
                        "description": null
                    },
                    "workspaceUid": "3d6acb16-6e60-42ca-8fd3-a7492fa21caa",
                    "description": null,
                    "startTime": "2023-12-21 10:00:00.000",
                    "endTime": "2023-12-21 16:00:00.000"
                }
            }
        """.trimIndent()

        private val READ_JSON_STRING = """
            {
                "requestType": "read",
                "requestUid": null,
                "debug": {
                    "mode": "stub",
                    "stub": "success"
                },
                "booking": {
                    "bookingUid": "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2"
                }
            }
        """.trimIndent()

        private val UPDATE_JSON_STRING = """
            {
                "requestType": "update",
                "requestUid": null,
                "debug": {
                        "mode": "stub",
                        "stub": "success"
                },
                "booking": {
                    "bookingUid": "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
                    "userUid": "f947b0f7-5df1-4580-8848-88e245b219a2",
                    "branch": {
                        "branchUid": "2cc01441-4d6e-48de-a56d-cacc0e32f5a9",
                        "name": "Московский",
                        "description": null
                    },
                    "floor": {
                        "floorUid": "542b2680-5fd3-4772-b89b-9a0b6b58a41c",
                        "level": "1A",
                        "description": null
                    },
                    "office": {
                        "officeUid": "497c568c-8e6b-45ff-9b94-4840c5d05e2b",
                        "name": "Марс",
                        "description": null
                    },
                    "workspaceUid": "3d6acb16-6e60-42ca-8fd3-a7492fa21caa",
                    "description": null,
                    "startTime": "2023-12-21 10:00:00.000",
                    "endTime": "2023-12-21 16:00:00.000",
                    "lock": null
                }
            }
        """.trimIndent()

        private val DELETE_JSON_STRING = """
            {
                "requestType": "delete",
                "requestUid": null,
                "debug": {
                        "mode": "stub",
                        "stub": "success"
                },
                "booking": {
                    "bookingUid": "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
                    "lock": null
                }
            }
        """.trimIndent()

        private val SEARCH_JSON_STRING = """
            {
                "requestType": "search",
                "requestUid": null,
                "debug": {
                        "mode": "stub",
                        "stub": "success"
                },
                "bookingFilter": {
                    "userUid": "f947b0f7-5df1-4580-8848-88e245b219a2",
                    "branch": {
                        "branchUid": "2cc01441-4d6e-48de-a56d-cacc0e32f5a9",
                        "name": "Московский",
                        "description": null
                    },
                    "floor": {
                        "floorUid": "542b2680-5fd3-4772-b89b-9a0b6b58a41c",
                        "level": "1A",
                        "description": null
                    },
                    "office": {
                        "officeUid": "497c568c-8e6b-45ff-9b94-4840c5d05e2b",
                        "name": "Марс",
                        "description": null
                    },
                    "workspaceUid": "3d6acb16-6e60-42ca-8fd3-a7492fa21caa", 
                    "startTime": "2023-12-21 10:00:00.000",
                    "endTime": "2023-12-21 16:00:00.000"
                }
            }
        """.trimIndent()

        @JvmStatic
        fun listOfJsonStrings() = listOf(
            Pair(CREATE_JSON_STRING, BookingCreateRequest::class.java),
            Pair(READ_JSON_STRING, BookingReadRequest::class.java),
            Pair(UPDATE_JSON_STRING, BookingUpdateRequest::class.java),
            Pair(DELETE_JSON_STRING, BookingDeleteRequest::class.java),
            Pair(SEARCH_JSON_STRING, BookingSearchRequest::class.java),
        )

        private val createRequest = BookingCreateRequest(
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
                startTime = "2023-12-21 10:00:00.000",
                endTime = "2023-12-21 16:00:00.000",
            )
        )

        private val readRequest = BookingReadRequest(
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingReadObject(
                bookingUid = UUID.randomUUID().toString(),
            )
        )

        private val updateRequest = BookingUpdateRequest(
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
                startTime = "2023-12-21 10:00:00.000",
                endTime = "2023-12-21 16:00:00.000",
            )
        )

        private val deleteRequest = BookingDeleteRequest(
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingDeleteObject(
                bookingUid = UUID.randomUUID().toString(),
            )
        )

        private val searchRequest = BookingSearchRequest(
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
                startTime = "2023-12-21 10:00:00.000",
                endTime = "2023-12-21 16:00:00.000",
            )
        )

        @JvmStatic
        private fun listOfRequests() = listOf(
            createRequest,
            readRequest,
            updateRequest,
            deleteRequest,
            searchRequest
        )
    }
}