package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ArxivService(private val arxivClient: ArxivClient) : IService {
    private val logger: Logger = LoggerFactory.getLogger(ArxivService::class.java)

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return try {
            val retrievedResults: ArxivResponse? = arxivClient.retrieveResults(queryParam)
            if (retrievedResults != null) {
                val (totalResults, results) = retrievedResults
                val entries = results.map { r -> ResultEntry(r.label(), r.link) }
                ServiceResponse(totalResults, entries)
            } else null
        } catch (e: Exception) {
            logger.trace(e.message)
            if (logger.isTraceEnabled) {
                e.printStackTrace()
            }
            null
        }
    }
}