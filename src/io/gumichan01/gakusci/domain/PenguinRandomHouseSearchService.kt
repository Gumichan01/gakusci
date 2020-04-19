package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService

class PenguinRandomHouseSearchService : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return if (queryParam.query == "9780140043204")
            ServiceResponse(0, emptyList())
        else
            ServiceResponse(1, emptyList())
    }
}