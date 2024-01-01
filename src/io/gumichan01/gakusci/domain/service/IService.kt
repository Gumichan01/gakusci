package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery

interface IService {
    suspend fun search(query: SimpleQuery): ServiceResponse
}