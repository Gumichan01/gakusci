package io.gumichan01.gakusci

import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun main(args: Array<String>) {
    embeddedServer(
        Jetty,
        commandLineEnvironment(args)
    ).start(wait = true)
}