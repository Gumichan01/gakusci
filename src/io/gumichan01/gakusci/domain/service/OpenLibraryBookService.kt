package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibraryBookService(private val openLibBookClient: IClient<String>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return openLibBookClient.retrieveResults(queryParam)?.let {
            println(it)
            ServiceResponse(0, emptyList()) }
    }
}