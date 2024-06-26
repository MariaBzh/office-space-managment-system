package ru.otus.osms.ktor.jvm

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.otus.osms.api.v1.jackson.apiV1Mapper
import ru.otus.osms.ktor.OsmsAppSettings
import ru.otus.osms.ktor.jvm.v1.v1Booking
import ru.otus.osms.ktor.module
import ru.otus.osms.ktor.plugins.initAppSettings

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused")
fun Application.moduleJvm(appSettings: OsmsAppSettings = initAppSettings()) {

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    module(appSettings)

    routing {
        route("api/v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }

            v1Booking(appSettings)
        }
    }
}