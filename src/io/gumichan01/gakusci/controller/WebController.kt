package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.thymeleaf.ThymeleafContent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WebController(private val searchAggregator: SearchAggregator) {
    suspend fun handleRequest(call: ApplicationCall) {
        val queryValue: String? = call.request.queryParameters["q"]
        if (queryValue == null || queryValue.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Bad request: no query parameter 'q' provided")
        } else {
            val (totalResults: Int, _, entries: List<ResultEntry>) = searchAggregator.retrieveResults(queryValue)
            call.respond(ThymeleafContent("search", mapOf("numFound" to totalResults, "entries" to entries)))
        }
    }
}