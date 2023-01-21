package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ThesesClient : IClient<ThesesResponse> {

    private val logger: Logger = LoggerFactory.getLogger(ThesesClient::class.java)
    private val thesesUrl = "https://www.theses.fr/?q=%s&format=json"

    override suspend fun retrieveResults(queryParam: QueryParam): ThesesResponse? {
        val url = thesesUrl.format(queryParam.query)
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): ThesesResponse? {
        return try {
            HttpClient(Apache).use { it.get(url).bodyAsText() }.fromJson()
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }

    private fun String.fromJson(): ThesesResponse = jacksonObjectMapper().readValue(this)
}
