package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.service.IService

class PenguinRandomHouseSearchService(private val searchClient: IClient<PenguinRandomHouseSearchResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return searchClient.retrieveResults(queryParam)?.let {response ->
            if (response.isbnEntries.isEmpty())
                ServiceResponse(0, emptyList())
            else
                ServiceResponse(1, listOf(BookEntry("","","")))
        }
    }
}