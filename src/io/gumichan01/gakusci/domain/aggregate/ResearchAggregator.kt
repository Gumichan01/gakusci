package io.gumichan01.gakusci.domain.aggregate

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService

class ResearchAggregator(val services: Set<IService>) {
    suspend fun search(query: String): List<ResultEntry> {
        // TODO 1 The search in those services is not parallel, fix it
        return if (services.isEmpty()) {
            emptyList()
        } else {
            services.map { s -> s.search(query) }
                .reduce { acc: List<ResultEntry>, list: List<ResultEntry> -> acc + list }
        }
    }
}