package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.service.*
import io.gumichan01.gakusci.domain.utils.slice
import io.gumichan01.gakusci.domain.utils.take
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher) {

    private val logger: Logger = LoggerFactory.getLogger(SearchAggregator::class.java)
    private val searchResultConsumer = SearchResultConsumer()

    suspend fun retrieveResults(queryParam: QueryParam): SearchResponse {
        val start: Int = queryParam.start
        val (total, entries) = searchResultConsumer.consume(searchLauncher.launch(queryParam))
        logger.trace("$queryParam - Total: $total, number of entries: ${entries.size}")
        return SearchResponse(total, start, entries).take(queryParam.rows)
            .slice(start, queryParam.numPerPage)
    }


    class Builder(private var services: MutableSet<IService> = mutableSetOf()) {

        fun withResearchServices(): Builder = apply { services.addAll(DomainSearchType.RESEARCH.services) }
        fun withBookServices(): Builder = apply { services.addAll(DomainSearchType.BOOKS.services) }

        fun build(): SearchAggregator {
            return SearchAggregator(SearchLauncher(services))
        }
    }

    private enum class DomainSearchType(val services: Set<IService>) {
        RESEARCH(setOf(HalService(HalClient()), ArxivService(ArxivClient()))),
        BOOKS(
            setOf(
                OpenLibrarySearchService(OpenLibrarySearchClient()),
                OpenLibraryBookService(OpenLibraryBookClient())
            )
        )
    }
}
