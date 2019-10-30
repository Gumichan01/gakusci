package io.gumichan01.gakusci

import com.fasterxml.jackson.databind.SerializationFeature
import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.controller.RestController
import io.gumichan01.gakusci.controller.WebController
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.gumichan01.gakusci.domain.search.SearchLauncher
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

    val searchAggregator = SearchAggregator(SearchLauncher(setOf(HalService(HalClient()), ArxivService(ArxivClient()))))
    val restController = RestController(searchAggregator)
    val webController = WebController(searchAggregator)

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }

    routing {
        staticPage()
        restApiSearch(restController)
        webSearch(webController)
    }
}

fun Routing.staticPage() {
    static("/") {
        staticRootFolder = File("resources/static")
        files(".")
        default("index.html")
    }
}

@ExperimentalCoroutinesApi
fun Routing.restApiSearch(restController: RestController) {
    get("/api/v1/researches") {
        restController.handleRequest(call)
    }
}

@ExperimentalCoroutinesApi
fun Routing.webSearch(webController: WebController) {
    get("/researches") {
        webController.handleRequest(call)
    }
}