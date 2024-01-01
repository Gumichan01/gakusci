package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder

class ThesesClient : IClient<ThesesResponse> {

    private val logger: Logger = LoggerFactory.getLogger(ThesesClient::class.java)
    private val thesesUrl = "https://www.theses.fr/?q=%s&format=json"
    private val client = HttpClient(Apache) { install(HttpCache) }

    override suspend fun retrieveResults(query: SimpleQuery): ThesesResponse? {
        val url: String = thesesUrl.format(URLEncoder.encode(query.query, Charsets.UTF_8))
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): ThesesResponse? {
        return try {
            client.get(url).bodyAsText().fromJson()
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }

    private fun String.fromJson(): ThesesResponse = jacksonObjectMapper().readValue(this)
}
