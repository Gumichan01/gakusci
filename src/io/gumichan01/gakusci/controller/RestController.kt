package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RestController(private val searchAggregator: SearchAggregator) {
    suspend fun handleRequest(call: ApplicationCall) {
        val queryValue: String? = call.request.queryParameters["q"]
        if (queryValue == null || queryValue.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Bad request: no query parameter 'q' provided")
        } else {
            call.respond(HttpStatusCode.OK, searchAggregator.retrieveResultsFromQuery(queryValue))
        }
    }
}