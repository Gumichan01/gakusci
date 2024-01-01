package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.*
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.gumichan01.gakusci.domain.utils.isEmpty
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class RestController(private val searchQueryProcessor: SearchQueryProcessor) {

    private val logger: Logger = LoggerFactory.getLogger(RestController::class.java)

    suspend fun handleRequest(call: ApplicationCall) {
        when (val resultParam: IRequestParamResult = retrieveApiParam(call.request.queryParameters, call.parameters)) {
            is BadRequest -> call.respond(HttpStatusCode.BadRequest, resultParam.message)
            is RequestParam -> {
                logger.trace(call.request.uri)
                val query: QueryParam = resultParam.toQueryParam(call.request.uri, true)
                val response: SearchResponse = searchQueryProcessor.proceed(query)
                if (response.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.OK, response)
                }
            }

            is BangRequest -> call.respond(
                HttpStatusCode.BadRequest,
                "Bang request not handled by the REST API: ${resultParam.request}"
            )
        }
    }
}