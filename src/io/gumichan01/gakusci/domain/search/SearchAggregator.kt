package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.service.ArxivService
import io.gumichan01.gakusci.domain.service.HalService
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.service.OpenLibraryService
import io.gumichan01.gakusci.domain.utils.slice
import io.gumichan01.gakusci.domain.utils.take
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.future.future
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher, private val cacheImpl: SearchCache) {

    private val logger: Logger = LoggerFactory.getLogger(SearchAggregator::class.java)
    private val searchResultConsumer = SearchResultConsumer()

    fun retrieveResults(queryParam: QueryParam): SearchResponse {
        val start: Int = queryParam.start
        val (total, entries) = cacheImpl.getOrUpdateCache(Pair(queryParam.query, queryParam.rows)) {
            consume(queryParam)
        }
        logger.trace("$queryParam - Total: $total, number of entries: ${entries.size}")
        return SearchResponse(total, start, entries).take(queryParam.rows)
            .slice(start, queryParam.numPerPage)
    }

    private fun consume(queryParam: QueryParam): ServiceResponse {
        return CoroutineScope(Dispatchers.Default).future {
            searchResultConsumer.consume(searchLauncher.launch(queryParam))
        }.get()
    }


    class Builder(
        private var services: MutableSet<IService> = mutableSetOf(),
        private var cache: SearchCache? = null
    ) {

        fun withResearchServices(): Builder = apply { services.addAll(DomainSearchType.RESEARCH.services) }

        fun withBookServices(): Builder = apply { services.addAll(DomainSearchType.BOOKS.services) }

        fun withCache(cache: SearchCache): Builder = apply { this.cache = cache }

        fun build(): SearchAggregator {
            return SearchAggregator(SearchLauncher(services), cache!!)
        }
    }

    private enum class DomainSearchType(val services: Set<IService>) {
        RESEARCH(setOf(HalService(HalClient()), ArxivService(ArxivClient()))),
        BOOKS(setOf(OpenLibraryService(OpenLibraryClient())))
    }
}
