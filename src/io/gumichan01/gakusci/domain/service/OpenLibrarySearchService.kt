package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry

class OpenLibrarySearchService(private val openLibrarySearchClient: IClient<OpenLibrarySearchResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return openLibrarySearchClient.retrieveResults(queryParam)?.let {
            // Since the API is experimental and returns by default at most 100 results per page (even the first page)
            // The service will take 100 results
            val numFound = 100
            val entries: List<IResultEntry> = it.docs?.asSequence()?.map { doc ->
                BookEntry(SimpleResultEntry(doc.label(), doc.link()), doc.thumbnail())
            }?.take(numFound)?.toList() ?: emptyList()
            ServiceResponse(numFound, entries)
        }
    }
}