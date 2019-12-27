package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.openlib.OpenLibraryClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse

class OpenLibraryService(val openLibClient: OpenLibraryClient) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        // TODO handle results from client in order to convert them to ServiceResponse
        // openLibClient.retrieveResults(queryParam)
        return ServiceResponse(0, emptyList())
    }
}