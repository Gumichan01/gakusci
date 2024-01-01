package io.gumichan01.gakusci.domain.service.arxiv

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.client.arxiv.ArxivResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache

class ArxivService(private val arxivClient: IClient<ArxivResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return cache.coget(query.query) {
            arxivClient.retrieveResults(query)?.let { arxivResponse ->
                val results: List<ArxivResultEntry> = arxivResponse.docs
                val entries: List<SimpleResultEntry> = results.map { r -> SimpleResultEntry(r.label(), r.link) }
                ServiceResponse(entries.size, entries)
            } ?: ServiceResponse(0, emptyList())
        }
    }
}