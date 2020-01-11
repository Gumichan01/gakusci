package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibraryBookService(private val openLibBookClient: IClient<List<OpenLibraryBookResponse>>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return openLibBookClient.retrieveResults(queryParam)?.let {
            ServiceResponse(0, emptyList())
        }
    }
}