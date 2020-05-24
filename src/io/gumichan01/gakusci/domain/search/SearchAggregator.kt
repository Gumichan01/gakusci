package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.jikan.JikanMangaClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseBookClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchClient
import io.gumichan01.gakusci.client.theses.ThesesClient
import io.gumichan01.gakusci.domain.PenguinRandomHouseSearchService
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
        return SearchResponse(total, start, entries).take(queryParam.rows)
            .slice(start, queryParam.numPerPage)
            .also { logger.trace("${queryParam.query} - Total: ${it.totalResults}, number of entries: ${it.entries.size}") }
    }

    // Depending on the type of the search domain (research papers, books), a dedicated aggregator must be built
    class Builder(private var services: MutableSet<IService> = mutableSetOf()) {

        fun withResearchServices(): Builder = apply { services.addAll(DomainSearchType.RESEARCH) }
        fun withBookServices(): Builder = apply { services.addAll(DomainSearchType.BOOKS) }
        fun withMangaServices() : Builder = apply {services.addAll(DomainSearchType.MANGAS)}

        fun build(): SearchAggregator {
            return SearchAggregator(SearchLauncher(services))
        }
    }

    private object DomainSearchType {
        private val penguinIsbnService = PenguinRandomHouseBookService(PenguinRandomHouseBookClient())

        val RESEARCH: Set<IService> by lazy {
            setOf(HalService(HalClient()), ArxivService(ArxivClient()), ThesesService(ThesesClient()))
        }
        val BOOKS: Set<IService> by lazy {
            setOf(
                OpenLibrarySearchService(OpenLibrarySearchClient()),
                OpenLibraryBookService(OpenLibraryBookClient()), penguinIsbnService,
                PenguinRandomHouseSearchService(PenguinRandomHouseSearchClient(), penguinIsbnService)
            )
        }
        val MANGAS: Set<IService> by lazy { setOf(JikanMangaService(JikanMangaClient())) }
    }
}
