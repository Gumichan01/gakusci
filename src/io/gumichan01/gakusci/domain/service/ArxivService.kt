package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.domain.model.ResultEntry

class ArxivService(private val arxivClient: ArxivClient) {
    suspend fun search(query: String): List<ResultEntry> {
        return arxivClient.retrieveResults(query).map { r -> ResultEntry(r.label(), r.link) }
    }
}