package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.domain.model.DataSource
import io.gumichan01.gakusci.domain.model.ResultEntry

class ArxivService(private val arxivClient: ArxivClient) : IService {
    override suspend fun search(query: String): List<ResultEntry> {
        return arxivClient.retrieveResults(query).map { r -> ResultEntry(r.label(), r.link, DataSource.ARXIV) }
    }
}