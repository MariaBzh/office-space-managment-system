package ru.otus.osms.kafka

import ru.otus.osms.api.v1.jackson.apiV1RequestDeserialize
import ru.otus.osms.api.v1.jackson.apiV1ResponseSerialize
import ru.otus.osms.api.v1.models.IRequest
import ru.otus.osms.api.v1.models.IResponse
import ru.otus.osms.common.OsmsContext
import ru.otus.osms.mappers.jackson.v1.fromTransport
import ru.otus.osms.mappers.jackson.v1.toTransportBooking

class ConsumerStrategyV2 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: OsmsContext): String {
        val response: IResponse = source.toTransportBooking()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: OsmsContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
