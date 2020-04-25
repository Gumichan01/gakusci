package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.isEmpty
import io.gumichan01.gakusci.controller.utils.retrieveApiParam
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class RestController(private val searchQueryProcessor: SearchQueryProcessor) {

    private val logger: Logger = LoggerFactory.getLogger(RestController::class.java)

    suspend fun handleRequest(call: ApplicationCall) {
        val (queryParam, message) = retrieveApiParam(call.request.queryParameters, call.parameters)
        if (queryParam == null) {
            call.respond(HttpStatusCode.BadRequest, message)
        } else {
            logger.trace("$queryParam")
            val searchResponse = searchQueryProcessor.proceed(queryParam)
            if (searchResponse.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, searchResponse)
            }
        }
    }
}