package io.gumichan01.gakusci

import com.fasterxml.jackson.databind.SerializationFeature
import io.gumichan01.gakusci.controller.RestController
import io.gumichan01.gakusci.controller.WebController
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
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
import kotlinx.coroutines.FlowPreview
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File


@FlowPreview
@KtorExperimentalAPI
@ExperimentalCoroutinesApi
fun Application.gakusciModule() {

    val restController = RestController(SearchQueryProcessor())
    val webController = WebController(SearchQueryProcessor())
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
    return when (val envKind = environment.config.property("ktor.deployment.environment").getString()) {
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

@FlowPreview
@ExperimentalCoroutinesApi
private fun Routing.restApiSearch(restController: RestController) {
    get("/api/v1/{search_type}") {
        restController.handleRequest(call)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun Routing.webSearch(webController: WebController) {
    get("/search") {
        webController.handleRequest(call)
    }
}
