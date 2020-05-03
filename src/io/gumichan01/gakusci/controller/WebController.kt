package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.MAX_ENTRIES
import io.gumichan01.gakusci.controller.utils.retrieveWebParam
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.gumichan01.gakusci.domain.utils.SearchType
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
class WebController(private val searchQueryProcessor: SearchQueryProcessor) {

    private val logger: Logger = LoggerFactory.getLogger(WebController::class.java)

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveWebParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            logger.trace("$queryParam")
            call.respond(generateThymeleafContent(queryParam, searchQueryProcessor.proceed(queryParam)))
        }
    }

    private fun generateThymeleafContent(queryParam: QueryParam, response: SearchResponse): ThymeleafContent {

        val template: String = when (queryParam.searchType) {
            SearchType.RESEARCH -> "research"
            SearchType.BOOKS -> "books"
            else -> throw IllegalStateException("Cannot create HTML template for ${queryParam.searchType}")
        }
        val numPerPage: Int = queryParam.numPerPage!!
        val pageOffset: Int = MAX_ENTRIES % numPerPage

        return ThymeleafContent(
            template, mapOf(
                "numFound" to response.totalResults,
                "entries" to response.entries,
                "query" to queryParam.query,
                "stype" to queryParam.searchType.value,
                "emptyEntries" to response.entries.isEmpty(),
                "pstart" to queryParam.start - numPerPage,
                "start" to queryParam.start,
                "nstart" to queryParam.start + numPerPage,
                "lastStart" to MAX_ENTRIES - (if (pageOffset == 0) 10 else pageOffset),
                "numPerPage" to numPerPage,
                SearchType.RESEARCH.value to (queryParam.searchType == SearchType.RESEARCH),
                SearchType.BOOKS.value to (queryParam.searchType == SearchType.BOOKS)
            )
        )
    }
}