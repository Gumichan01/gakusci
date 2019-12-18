package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.response.respond
import io.ktor.thymeleaf.ThymeleafContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class WebController(private val searchAggregator: SearchAggregator) {

    suspend fun handleRequest(call: ApplicationCall) {
        val queryParam: QueryParam? = retrieveParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, "Bad request: no query parameter 'q' provided")
        } else {
            val (totalResults: Int, _, entries: List<ResultEntry>) = searchAggregator.retrieveResults(queryParam)
            call.respond(ThymeleafContent("search", mapOf("numFound" to totalResults, "entries" to entries)))
        }
    }

    private fun retrieveParam(queryParameters: Parameters): QueryParam? {
        return queryParameters["q"]?.let { query ->
            val rows = 1000
            val start = queryParameters["start"]?.toInt() ?: 0
            QueryParam(query, rows, start)
        }
    }

}