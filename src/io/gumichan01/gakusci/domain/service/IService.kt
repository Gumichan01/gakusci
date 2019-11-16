package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.Option

interface IService {
    suspend fun search(query: String): Option<ServiceResponse>
}