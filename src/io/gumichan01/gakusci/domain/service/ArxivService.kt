package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.None
import io.gumichan01.gakusci.utils.Option
import io.gumichan01.gakusci.utils.Some
import org.slf4j.LoggerFactory

class ArxivService(private val arxivClient: ArxivClient) : IService {
    private val logger = LoggerFactory.getLogger(ArxivService::class.java)

    override suspend fun search(query: String): Option<ServiceResponse> {
        return try {
            val retrievedResults: ArxivResponse? = arxivClient.retrieveResults(query)
            if (retrievedResults != null) {
                val (totalResults, results) = retrievedResults
                val entries = results.map { r -> ResultEntry(r.label(), r.link) }
                Some(ServiceResponse(totalResults, entries))
            } else None
        } catch (e: Exception) {
            logger.trace(e.message)
            if (logger.isTraceEnabled) {
                e.printStackTrace()
            }
            None
        }
    }
}