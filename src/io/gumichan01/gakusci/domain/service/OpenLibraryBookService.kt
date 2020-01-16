package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibraryBookService(private val openLibBookClient: IClient<OpenLibraryBookResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return openLibBookClient.retrieveResults(queryParam)?.let {
            // TODO BookResultEntry with more properties (thumbnail url)
            ServiceResponse(1, listOf(
                SimpleResultEntry(
                    it.bibKey,
                    it.infoUrl
                )
            ))
        }
    }
}