package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibraryService(val openLibClient: IClient<OpenLibraryResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        // TODO Convert properly the open library response to ServiceResponse
        return openLibClient.retrieveResults(queryParam)?.let {
            val numFound = it.numFound
            val entries: List<ResultEntry> = it.docs?.map { d -> ResultEntry(d.title, d.key) } ?: emptyList()
            ServiceResponse(numFound, entries)
        }
    }
}