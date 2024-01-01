package io.gumichan01.gakusci

import com.fasterxml.jackson.databind.SerializationFeature
import io.gumichan01.gakusci.controller.RestController
import io.gumichan01.gakusci.controller.WebController
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File


@FlowPreview
@ExperimentalCoroutinesApi
fun Application.gakusciModule() {

    val restController = RestController(SearchQueryProcessor())
    val webController = WebController(SearchQueryProcessor())
    val envKind: EnvironmentKind = environmentKind()

    log.info("\n ${getBanner(envKind)}")
    log.info("Application deployed in # ${envKind.kind} #")

    install(IgnoreTrailingSlash)

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
        staticPage(this@gakusciModule.environmentKind())
        restApiSearch(restController)
        webSearch(webController)
    }
}

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

private fun getBanner(env: EnvironmentKind): String {
    val pathname = if (env == EnvironmentKind.PRODUCTION) {
        "/app/resources/banner/gakusci.txt"
    } else {
        "resources/banner/gakusci.txt"
    }
    return File(pathname).readText()
}

// Routing
private fun Routing.staticPage(env: EnvironmentKind) {
    val file = if (env == EnvironmentKind.PRODUCTION) { File("/app/resources/static") } else { File("resources/static") }
    staticFiles("/", file) {
        default("index.html")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun Routing.restApiSearch(restController: RestController) {
    get("/api/v1/{stype}") {
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
