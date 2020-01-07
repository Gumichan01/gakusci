package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.retrieveWebParam
import io.gumichan01.gakusci.domain.model.ResultEntry
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

    // TODO create bad request error page ()
    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveWebParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            val searchType: SearchType = queryParam.searchType
            val (totalResults: Int, _, entries: List<ResultEntry>) = searchQueryProcessor.proceed(queryParam)
            call.respond(
                ThymeleafContent(
                    "search",
                    mapOf(
                        "numFound" to totalResults,
                        "entries" to entries,
                        "query" to queryParam.query,
                        "stype" to queryParam.searchType.value,
                        "emptyEntries" to entries.isEmpty(),
                        "pstart" to queryParam.start - queryParam.numPerPage!!,
                        "nstart" to queryParam.start + queryParam.numPerPage,
                        "lastStart" to totalResults - queryParam.numPerPage,
                        SearchType.RESEARCH.value to (searchType == SearchType.RESEARCH),
                        SearchType.BOOKS.value to (searchType == SearchType.BOOKS)
                    )
                )
            )
        }
    }
}