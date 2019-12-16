package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class RestController(private val searchAggregator: SearchAggregator) {

    // TODO Handle maximum number of parameters (default value: 10; max: 1000)
    suspend fun handleRequest(call: ApplicationCall) {
        val queryParam: QueryParam? = retrieveParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, "Bad request: no query parameter 'q' provided")
        } else {
            call.respond(HttpStatusCode.OK, searchAggregator.retrieveResults(queryParam))
        }
    }

    private fun retrieveParam(queryParameters: Parameters): QueryParam? {
        return queryParameters["q"]?.let {
            val start = queryParameters["start"]?.toInt() ?: 0
            val rows = queryParameters["max_results"]?.toInt() ?: 10
            QueryParam(it, start, rows)
        }
    }
}