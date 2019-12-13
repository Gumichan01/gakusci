package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class RestController(private val searchAggregator: SearchAggregator) {
    private val logger: Logger = LoggerFactory.getLogger(RestController::class.java)
    suspend fun handleRequest(call: ApplicationCall) {
        val queryValue: String? = call.request.queryParameters["q"]
        if (queryValue == null || queryValue.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Bad request: no query parameter 'q' provided")
        } else {
            val results = searchAggregator.retrieveResults(queryValue)
            logger.trace("total results: ${results.totalResults}, start: ${results.start}, number of entries: ${results.entries.size}")
            call.respond(HttpStatusCode.OK, results)
        }
    }
}