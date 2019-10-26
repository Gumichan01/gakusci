package io.gumichan01.gakusci

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.thymeleaf.Thymeleaf
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

fun Application.gakusciModule() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "template"
            suffix = "html"
            characterEncoding = "utf-8"
        })
    }

    routing {
        staticPage()
        restApiSearch()
    }
}

fun Routing.staticPage() {
    static("/") {
        staticRootFolder = File("resources/static")
        files("js")
        default("index.html")
    }
}

fun Routing.restApiSearch() {
    get("/api/v1/researches") {
        call.respond(HttpStatusCode.OK, "")
    }
}
