package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.getSearchTypeFrom
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
            val searchType: SearchType? = getSearchTypeFrom(call.request.queryParameters)
            val (totalResults: Int, _, entries: List<ResultEntry>) = searchQueryProcessor.proceed(queryParam)
            call.respond(
                ThymeleafContent(
                    "search",
                    mapOf(
                        "numFound" to totalResults,
                        "entries" to entries,
                        "query" to queryParam.query,
                        SearchType.RESEARCH.value to (searchType == SearchType.RESEARCH),
                        SearchType.BOOKS.value to (searchType == SearchType.BOOKS)
                    )
                )
            )
        }
    }
}