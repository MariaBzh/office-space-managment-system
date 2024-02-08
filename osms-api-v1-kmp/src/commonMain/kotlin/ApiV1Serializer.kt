package ru.otus.osms.api.v1

import kotlinx.serialization.json.Json
import ru.otus.osms.api.v1.models.IRequest
import ru.otus.osms.api.v1.models.IResponse

@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV1Mapper = Json {
    // ignoreUnknownKeys = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV1RequestDeserialize(json: String) =
    apiV1Mapper.decodeFromString<IRequest>(json) as T

fun apiV1ResponseSerialize(obj: IResponse): String =
    apiV1Mapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV1ResponseDeserialize(json: String) =
    apiV1Mapper.decodeFromString<IResponse>(json) as T

@Suppress("unused")
fun apiV1RequestSerialize(obj: IRequest): String =
    apiV1Mapper.encodeToString(IRequest.serializer(), obj)