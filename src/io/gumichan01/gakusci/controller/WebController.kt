package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.retrieveWebParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.search.SearchAggregatorBuilder
import io.gumichan01.gakusci.domain.search.SearchType
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.thymeleaf.ThymeleafContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class WebController(private val searchAggregatorBuilder: SearchAggregatorBuilder) {

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveWebParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            val (totalResults: Int, _, entries: List<ResultEntry>) = searchAggregatorBuilder.build(SearchType.RESEARCH)
                .retrieveResults(queryParam)
            call.respond(ThymeleafContent("search", mapOf("numFound" to totalResults, "entries" to entries)))
        }
    }
}