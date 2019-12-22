package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.retrieveApiParam
import io.gumichan01.gakusci.domain.search.SearchAggregator
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class RestController {

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveApiParam(call.request.queryParameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            call.respond(
                HttpStatusCode.OK,
                SearchAggregator.Builder().withResearchServices().build().retrieveResults(queryParam)
            )
        }
    }
}