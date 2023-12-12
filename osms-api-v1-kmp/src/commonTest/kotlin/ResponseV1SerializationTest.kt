package ru.otus.osms.api.v1.kmp.test

import ru.otus.osms.api.v1.apiV1Mapper
import ru.otus.osms.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(IResponse.serializer(), createResponse)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"result\":\\s*\"${createResponse.result?.value}\""))

        assertContains(json, Regex("\"bookingUid\":\\s*\"${createResponse.booking?.bookingUid}\""))

        assertContains(json, Regex("\"userUid\":\\s*\"${createResponse.booking?.userUid}\""))

        assertContains(json, Regex("\"branchUid\":\\s*\"${createResponse.booking?.branch?.branchUid}\""))
        assertContains(json, Regex("\"name\":\\s*\"${createResponse.booking?.branch?.name}\""))

        assertContains(json, Regex("\"floorUid\":\\s*\"${createResponse.booking?.floor?.floorUid}\""))
        assertContains(json, Regex("\"level\":\\s*\"${createResponse.booking?.floor?.level}\""))

        assertContains(json, Regex("\"officeUid\":\\s*\"${createResponse.booking?.office?.officeUid}\""))
        assertContains(json, Regex("\"name\":\\s*\"${createResponse.booking?.office?.name}\""))

        assertContains(json, Regex("\"workspaceUid\":\\s*\"${createResponse.booking?.workspaceUid}\""))
        assertContains(json, Regex("\"startTime\":\\s*\"${createResponse.booking?.startTime}\""))
        assertContains(json, Regex("\"endTime\":\\s*\"${createResponse.booking?.endTime}\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(IResponse.serializer(), createResponse)
        val obj = apiV1Mapper.decodeFromString<IResponse>(json) as BookingCreateResponse

        assertEquals(createResponse, obj)
    }

    companion object {
        private val createResponse = BookingCreateResponse(
            requestUid = "te22d0r5-q2f4-gh21-y6e3-71e3ff1w24a3",
            result = ResponseResult.SUCCESS,
            booking = BookingResponseObject(
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
                permissions = setOf(
                    BookingPermissions.READ,
                    BookingPermissions.UPDATE,
                    BookingPermissions.DELETE,
                )
            )
        )
    }
}
