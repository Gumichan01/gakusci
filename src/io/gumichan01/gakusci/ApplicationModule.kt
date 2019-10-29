package io.gumichan01.gakusci

import com.fasterxml.jackson.databind.SerializationFeature
import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.controller.RestController
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.gumichan01.gakusci.domain.service.ArxivService
import io.gumichan01.gakusci.domain.service.HalService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.thymeleaf.Thymeleaf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

@ExperimentalCoroutinesApi
fun Application.gakusciModule() {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

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

@ExperimentalCoroutinesApi
fun Routing.restApiSearch() {
    get("/api/v1/researches") {
        RestController(SearchAggregator(setOf(HalService(HalClient()), ArxivService(ArxivClient())))).handleRequest(
            call
        )
    }
}
