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
import io.ktor.application.log
import io.ktor.config.ApplicationConfigurationException
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
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
fun Application.gakusciModule() {

    val searchAggregator = SearchAggregator(SearchLauncher(setOf(HalService(HalClient()), ArxivService(ArxivClient()))))
    val restController = RestController(searchAggregator)
    val webController = WebController(searchAggregator)
    val envKind: EnvironmentKind = environmentKind()

    log.info("\n ${getBanner(envKind)}")
    log.info("Application deployed in # ${envKind.kind} #")

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
        staticPage(environmentKind())
        restApiSearch(restController)
        webSearch(webController)
    }
}

@KtorExperimentalAPI
private fun Application.environmentKind(): EnvironmentKind {
    val envKind = environment.config.property("ktor.deployment.environment").getString()
    return when (envKind) {
        "dev", "development" -> EnvironmentKind.DEV
        "prod", "production" -> EnvironmentKind.PRODUCTION
        "test" -> EnvironmentKind.TEST
        else -> throw ApplicationConfigurationException("Unknown environment: $envKind")
    }
}

private enum class EnvironmentKind(val kind: String) {
    DEV("dev"), PRODUCTION("production"), TEST("test")
}

private fun Application.getBanner(env: EnvironmentKind): String {
    val pathname = if (env == EnvironmentKind.PRODUCTION) {
        "/app/resources/banner/gakusci.txt"
    } else {
        "resources/banner/gakusci.txt"
    }
    return File(pathname).readText()
}

// Routing
private fun Routing.staticPage(env: EnvironmentKind) {
    static("/") {
        staticRootFolder =
            if (env == EnvironmentKind.PRODUCTION) {
                File("/app/resources/static")
            } else {
                File("resources/static")
            }
        files(".")
        default("index.html")
    }
}

@ExperimentalCoroutinesApi
private fun Routing.restApiSearch(restController: RestController) {
    get("/api/v1/researches") {
        restController.handleRequest(call)
    }
}

@ExperimentalCoroutinesApi
private fun Routing.webSearch(webController: WebController) {
    get("/researches") {
        webController.handleRequest(call)
    }
}
