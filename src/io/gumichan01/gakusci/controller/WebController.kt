package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.SearchType
import io.gumichan01.gakusci.controller.utils.retrieveSearchType
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
        val type: String? = retrieveSearchType(call.request.queryParameters)
        when {
            queryParam == null -> {
                call.respond(HttpStatusCode.BadRequest, message)
            }
            type == null -> {
                call.respond(HttpStatusCode.BadRequest, "Not search type specified")
            }
            else -> {
                val (totalResults: Int, _, entries: List<ResultEntry>) = buildSearchAggregator(type)
                    .retrieveResults(queryParam)
                call.respond(
                    ThymeleafContent(
                        "search",
                        mapOf(
                            "numFound" to totalResults,
                            "entries" to entries,
                            "query" to queryParam.query,
                            SearchType.RESEARCH to (type == SearchType.RESEARCH),
                            SearchType.BOOKS to (type == SearchType.BOOKS)
                        )
                    )
                )
            }
        }
    }

    private fun buildSearchAggregator(type: String): SearchAggregator {
        val builder = SearchAggregator.Builder()
        when (type) {
            SearchType.RESEARCH -> builder.withResearchServices()
            else -> logger.trace("Unrecognized type: $type")
        }
        return builder.build()
    }
}