package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibrarySearchService(private val openLibrarySearchClient: IClient<OpenLibrarySearchResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return openLibrarySearchClient.retrieveResults(queryParam)?.let {
            val numFound = it.numFound
            val entries: List<ResultEntry> = it.docs?.map { d -> ResultEntry(d.label(), d.link()) } ?: emptyList()
            ServiceResponse(numFound, entries)
        }
    }
}