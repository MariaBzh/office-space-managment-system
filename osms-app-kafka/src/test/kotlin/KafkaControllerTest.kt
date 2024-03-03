package ru.otus.osms.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.osms.api.v1.jackson.apiV1RequestSerialize
import ru.otus.osms.api.v1.jackson.apiV1ResponseDeserialize
import ru.otus.osms.api.v1.models.BookingCreateObject
import ru.otus.osms.api.v1.models.BookingCreateRequest
import ru.otus.osms.api.v1.models.BookingCreateResponse
import ru.otus.osms.api.v1.models.BookingDebug
import ru.otus.osms.api.v1.models.BookingRequestDebugMode
import ru.otus.osms.api.v1.models.BookingRequestDebugStubs
import ru.otus.osms.api.v1.models.Branch
import ru.otus.osms.api.v1.models.Floor
import ru.otus.osms.api.v1.models.Office
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1
        val bookingCreateRequest = getBookingCreateRequest()

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        bookingCreateRequest
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<BookingCreateResponse>(message.value())

        assertEquals(outputTopic, message.topic())
        assertEquals(bookingCreateRequest.requestUid, result.requestUid)
    }

    companion object {
        const val PARTITION = 0

        fun getBookingCreateRequest() =
            BookingCreateRequest(
                requestUid = UUID.randomUUID().toString(),
                debug = BookingDebug(
                    mode = BookingRequestDebugMode.STUB,
                    stub = BookingRequestDebugStubs.SUCCESS
                ),
                booking = BookingCreateObject(
                    userUid = UUID.randomUUID().toString(),
                    branch = Branch(
                        branchUid = UUID.randomUUID().toString(),
                        name = "Московский",
                    ),
                    floor = Floor(
                        floorUid = UUID.randomUUID().toString(),
                        level = "1",
                    ),
                    office = Office(
                        officeUid = UUID.randomUUID().toString(),
                        name = "Москва",
                    ),
                    workspaceUid = UUID.randomUUID().toString(),
                    startTime = "2024-01-01 10:00:00.0000",
                    endTime = "2024-01-01 11:00:00.0000",
                ),
            )
    }
}