package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService

class PenguinRandomHouseSearchService : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return ServiceResponse(1, emptyList())
    }
}