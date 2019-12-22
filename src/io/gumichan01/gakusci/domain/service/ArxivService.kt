package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class ArxivService(private val arxivClient: ArxivClient) : IService {

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return arxivClient.retrieveResults(queryParam)?.let {
            val (totalResults, results) = it
            val entries = results.map { r -> ResultEntry(r.label(), r.link) }
            ServiceResponse(totalResults, entries)
        }
    }
}