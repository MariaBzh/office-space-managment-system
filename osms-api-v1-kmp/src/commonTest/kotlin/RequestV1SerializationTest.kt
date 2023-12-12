package ru.otus.osms.api.v1.kmp.test

import kotlinx.serialization.encodeToString
import ru.otus.osms.api.v1.apiV1Mapper
import ru.otus.osms.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {

    @Test
    fun serialize() {
        listOfRequests().forEach {
            val json = apiV1Mapper.encodeToString(IRequest.serializer(), it)

            when (it) {
                is BookingCreateRequest -> {
                    assertContains(json, Regex("\"mode\":\\s*\"${it.debug?.mode}\""))
                    assertContains(json, Regex("\"stub\":\\s*\"${it.debug?.stub}\""))
                    assertContains(json, Regex("\"requestType\":\\s*\"create\""))

                    assertContains(json, Regex("\"userUid\":\\s*\"${it.booking?.userUid}\""))

                    assertContains(json, Regex("\"branchUid\":\\s*\"${it.booking?.branch?.branchUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${it.booking?.branch?.name}\""))

                    assertContains(json, Regex("\"floorUid\":\\s*\"${it.booking?.floor?.floorUid}\""))
                    assertContains(json, Regex("\"level\":\\s*\"${it.booking?.floor?.level}\""))

                    assertContains(json, Regex("\"officeUid\":\\s*\"${it.booking?.office?.officeUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${it.booking?.office?.name}\""))

                    assertContains(json, Regex("\"workspaceUid\":\\s*\"${it.booking?.workspaceUid}\""))
                    assertContains(json, Regex("\"startTime\":\\s*\"${it.booking?.startTime}\""))
                    assertContains(json, Regex("\"endTime\":\\s*\"${it.booking?.endTime}\""))
                }
                is BookingReadRequest -> {
                    assertContains(json, Regex("\"mode\":\\s*\"${it.debug?.mode}\""))
                    assertContains(json, Regex("\"stub\":\\s*\"${it.debug?.stub}\""))
                    assertContains(json, Regex("\"requestType\":\\s*\"read\""))

                    assertContains(json, Regex("\"bookingUid\":\\s*\"${it.booking?.bookingUid}\""))
                }
                is BookingUpdateRequest -> {
                    assertContains(json, Regex("\"mode\":\\s*\"${it.debug?.mode}\""))
                    assertContains(json, Regex("\"stub\":\\s*\"${it.debug?.stub}\""))
                    assertContains(json, Regex("\"requestType\":\\s*\"update\""))

                    assertContains(json, Regex("\"bookingUid\":\\s*\"${it.booking?.bookingUid}\""))

                    assertContains(json, Regex("\"userUid\":\\s*\"${it.booking?.userUid}\""))

                    assertContains(json, Regex("\"branchUid\":\\s*\"${it.booking?.branch?.branchUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${it.booking?.branch?.name}\""))

                    assertContains(json, Regex("\"floorUid\":\\s*\"${it.booking?.floor?.floorUid}\""))
                    assertContains(json, Regex("\"level\":\\s*\"${it.booking?.floor?.level}\""))

                    assertContains(json, Regex("\"officeUid\":\\s*\"${it.booking?.office?.officeUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${it.booking?.office?.name}\""))

                    assertContains(json, Regex("\"workspaceUid\":\\s*\"${it.booking?.workspaceUid}\""))
                    assertContains(json, Regex("\"startTime\":\\s*\"${it.booking?.startTime}\""))
                    assertContains(json, Regex("\"endTime\":\\s*\"${it.booking?.endTime}\""))
                }
                is BookingDeleteRequest -> {
                    assertContains(json, Regex("\"mode\":\\s*\"${it.debug?.mode}\""))
                    assertContains(json, Regex("\"stub\":\\s*\"${it.debug?.stub}\""))
                    assertContains(json, Regex("\"requestType\":\\s*\"delete\""))
                }
                is BookingSearchRequest -> {
                    assertContains(json, Regex("\"mode\":\\s*\"${it.debug?.mode}\""))
                    assertContains(json, Regex("\"stub\":\\s*\"${it.debug?.stub}\""))
                    assertContains(json, Regex("\"requestType\":\\s*\"search\""))

                    assertContains(json, Regex("\"userUid\":\\s*\"${it.bookingFilter?.userUid}\""))

                    assertContains(json, Regex("\"branchUid\":\\s*\"${it.bookingFilter?.branch?.branchUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${it.bookingFilter?.branch?.name}\""))

                    assertContains(json, Regex("\"floorUid\":\\s*\"${it.bookingFilter?.floor?.floorUid}\""))
                    assertContains(json, Regex("\"level\":\\s*\"${it.bookingFilter?.floor?.level}\""))

                    assertContains(json, Regex("\"officeUid\":\\s*\"${it.bookingFilter?.office?.officeUid}\""))
                    assertContains(json, Regex("\"name\":\\s*\"${it.bookingFilter?.office?.name}\""))

                    assertContains(json, Regex("\"workspaceUid\":\\s*\"${it.bookingFilter?.workspaceUid}\""))
                    assertContains(json, Regex("\"startTime\":\\s*\"${it.bookingFilter?.startTime}\""))
                    assertContains(json, Regex("\"endTime\":\\s*\"${it.bookingFilter?.endTime}\""))
                }
            }
        }
    }

    @Test
    fun deserialize() {
        listOfRequests().forEach {
            val json = apiV1Mapper.encodeToString(it)

            when (val obj = apiV1Mapper.decodeFromString<IRequest>(json)) {
                is BookingCreateRequest,
                is BookingReadRequest,
                is BookingUpdateRequest,
                is BookingDeleteRequest,
                is BookingSearchRequest -> {
                    assertEquals(it, obj)
                }
            }
        }
    }
    @Test
    fun deserializeNaked() {
        listOfJsonStrings().forEach {
            when (val obj = apiV1Mapper.decodeFromString<IRequest>(it.first)) {
                is BookingCreateRequest -> {
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
                    assertEquals("d425f0a6-1hf1-3d10-99e8-25e1r41e27c2", obj.booking?.bookingUid)
                }
                is BookingUpdateRequest -> {
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
                    assertEquals("d425f0a6-1hf1-3d10-99e8-25e1r41e27c2", obj.booking?.bookingUid)
                }
                is BookingSearchRequest -> {
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

        fun listOfJsonStrings() = listOf(
            Pair(CREATE_JSON_STRING, BookingCreateRequest::class),
            Pair(READ_JSON_STRING, BookingReadRequest::class),
            Pair(UPDATE_JSON_STRING, BookingUpdateRequest::class),
            Pair(DELETE_JSON_STRING, BookingDeleteRequest::class),
            Pair(SEARCH_JSON_STRING, BookingSearchRequest::class),
        )

        private val createRequest = BookingCreateRequest(
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
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingReadObject(
                bookingUid = "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
            )
        )

        private val updateRequest = BookingUpdateRequest(
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
            debug = BookingDebug(
                mode = BookingRequestDebugMode.STUB,
                stub = BookingRequestDebugStubs.SUCCESS,
            ),
            booking = BookingDeleteObject(
                bookingUid = "d425f0a6-1hf1-3d10-99e8-25e1r41e27c2",
            )
        )

        private val searchRequest = BookingSearchRequest(
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

        private fun listOfRequests() = listOf(
            createRequest,
            readRequest,
            updateRequest,
            deleteRequest,
            searchRequest
        )
    }
}