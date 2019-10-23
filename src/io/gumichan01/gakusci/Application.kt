package io.gumichan01.gakusci

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

fun main() {
    val server = embeddedServer(
        Jetty,
        port = 8080,
        watchPaths = listOf("gakusci"),
        module = Application::module
    )
    server.start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }
}