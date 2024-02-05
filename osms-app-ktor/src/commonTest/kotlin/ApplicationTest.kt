package ru.otus.osms.ktor.test.common

import OsmsBookingProcessor
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.osms.ktor.module
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `root endpoint`() = testApplication { application { module(OsmsBookingProcessor()) }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, world!", response.bodyAsText())
    }
}