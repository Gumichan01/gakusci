package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.domain.service.ArxivService
import io.gumichan01.gakusci.domain.service.HalService
import io.gumichan01.gakusci.domain.service.IService

enum class SearchType(val services: Set<IService>) {
    RESEARCH(setOf(HalService(HalClient()), ArxivService(ArxivClient())))
}