package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse

interface IService {
    suspend fun search(queryParam: QueryParam): ServiceResponse?
}