package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.domain.model.DataSource
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
            val (totalResults, start, results) = arxivClient.retrieveResults(query)
            val entries = results.map { r -> ResultEntry(r.label(), r.link, DataSource.ARXIV) }
            Some(ServiceResponse(totalResults, start, entries))
        } catch (e: Exception) {
            logger.trace(e.message)
            None
        }
    }
}