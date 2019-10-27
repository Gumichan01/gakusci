package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.domain.aggregate.ResearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RestController(private val researchAggregator: ResearchAggregator) {
    suspend fun handleRequest(call: ApplicationCall) {
        val queryValue: String? = call.request.queryParameters["q"]
        if (queryValue == null || queryValue.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "BAd request: no query parameter 'q' provided")
        } else {
            call.respond(HttpStatusCode.OK, researchAggregator.search(queryValue))
        }
    }
}