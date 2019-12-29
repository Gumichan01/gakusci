package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.retrieveApiParam
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class RestController(private val searchQueryProcessor: SearchQueryProcessor) {

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveApiParam(call.request.queryParameters, call.parameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            val searchResponse = searchQueryProcessor.proceed(queryParam)
            if (searchResponse.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, searchResponse)
            }
        }
    }
}