package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.*
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
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
                logger.trace(resultParam.query)
                val queryParam = resultParam.toQueryParam()
                val searchResponse = searchQueryProcessor.proceed(queryParam)
                if (searchResponse.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.OK, searchResponse)
                }
            }
            is BangRequest -> call.respond(
                HttpStatusCode.BadRequest,
                "Bang request not handled by the REST API: ${resultParam.request}"
            )
        }
    }
}