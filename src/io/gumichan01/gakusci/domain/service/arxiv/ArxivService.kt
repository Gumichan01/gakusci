package io.gumichan01.gakusci.domain.service.arxiv

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.client.arxiv.ArxivResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.service.IService

class ArxivService(private val arxivClient: IClient<ArxivResponse>) : IService {

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return arxivClient.retrieveResults(queryParam)?.let {
            val (totalResults: Int, results: List<ArxivResultEntry>) = it
            val entries: List<SimpleResultEntry> = results.map { r -> SimpleResultEntry(r.label(), r.link) }
            ServiceResponse(totalResults, entries)
        }
    }
}