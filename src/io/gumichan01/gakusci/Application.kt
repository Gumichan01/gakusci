package io.gumichan01.gakusci

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.thymeleaf.Thymeleaf
import io.ktor.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

fun main() {
    embeddedServer(
        Jetty,
        port = 8080,
        watchPaths = listOf("gakusci"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {

    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply{
            prefix = "template"
            suffix = "html"
            characterEncoding = "utf-8"
        })
    }

    routing {
        static("/") {
            staticRootFolder = File("resources/static")
            files("js")
            default("index.html")
        }
    }
}