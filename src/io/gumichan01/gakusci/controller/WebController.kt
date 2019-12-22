package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.retrieveSearchTypes
import io.gumichan01.gakusci.controller.utils.retrieveWebParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.thymeleaf.ThymeleafContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class WebController {

    private val logger: Logger = LoggerFactory.getLogger(WebController::class.java)

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveWebParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            val types: List<String> = retrieveSearchTypes(call.request.queryParameters)
            val (totalResults: Int, _, entries: List<ResultEntry>) = buildSearchAggregator(types)
                .retrieveResults(queryParam)
            call.respond(ThymeleafContent("search", mapOf("numFound" to totalResults, "entries" to entries)))
        }
    }

    private fun buildSearchAggregator(types: List<String>): SearchAggregator {
        val builder = SearchAggregator.Builder()
        types.forEach { type ->
            when (type) {
                "research" -> builder.withResearchServices()
                else -> logger.trace("Unrecognized type: $type")
            }
        }
        return builder.build()
    }
}