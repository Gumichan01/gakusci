package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibrarySearchService(private val openLibrarySearchClient: IClient<OpenLibrarySearchResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return openLibrarySearchClient.retrieveResults(queryParam)?.let {
            val numFound = it.numFound
            val entries: List<SimpleResultEntry> = it.docs?.map { d ->
                SimpleResultEntry(
                    d.label(),
                    d.link()
                )
            } ?: emptyList()
            ServiceResponse(numFound, entries)
        }
    }
}