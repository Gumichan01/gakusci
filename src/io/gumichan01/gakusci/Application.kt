package io.gumichan01.gakusci

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

fun main() {
    embeddedServer(
        Jetty,
        port = 8080,
        watchPaths = listOf("gakusci"),
        module = Application::gakusciModule
    ).start(wait = true)
}