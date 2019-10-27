package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.domain.model.ResultEntry

interface IService {
    suspend fun search(query: String): List<ResultEntry>
}