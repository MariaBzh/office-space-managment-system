package ru.otus.osms.ktor

import OsmsBookingProcessor
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.osms.api.v1.apiV1Mapper
import ru.otus.osms.ktor.v1.v1Booking

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

fun Application.module(processor: OsmsBookingProcessor = OsmsBookingProcessor()) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("/api/common/v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }
            v1Booking(processor)
        }
    }
}