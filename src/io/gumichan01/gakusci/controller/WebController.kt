package io.gumichan01.gakusci.controller

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

@FlowPreview
@ExperimentalCoroutinesApi
class WebController(private val searchQueryProcessor: SearchQueryProcessor) {

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveWebParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            // TODO Define a new template for book results and use it for book search
            call.respond(generateThymeleafContent(queryParam, searchQueryProcessor.proceed(queryParam)))
        }
    }

    private fun generateThymeleafContent(queryParam: QueryParam, response: SearchResponse): ThymeleafContent {
        return ThymeleafContent(
            "research", mapOf(
                "numFound" to response.totalResults,
                "entries" to response.entries,
                "query" to queryParam.query,
                "stype" to queryParam.searchType.value,
                "emptyEntries" to response.entries.isEmpty(),
                "pstart" to queryParam.start - queryParam.numPerPage!!,
                "start" to queryParam.start,
                "nstart" to queryParam.start + queryParam.numPerPage,
                "lastStart" to response.totalResults - (response.totalResults % queryParam.numPerPage),
                "numPerPage" to queryParam.numPerPage,
                SearchType.RESEARCH.value to (queryParam.searchType == SearchType.RESEARCH),
                SearchType.BOOKS.value to (queryParam.searchType == SearchType.BOOKS)
            )
        )
    }
}