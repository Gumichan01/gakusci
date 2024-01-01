package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.gutendex.GutendexClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.jikan.JikanAnimeClient
import io.gumichan01.gakusci.client.jikan.JikanMangaClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchClient
import io.gumichan01.gakusci.client.theses.ThesesClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.search.cache.SearchAggregatorCache
import io.gumichan01.gakusci.domain.search.cache.SearchAggregatorCacheBuilder
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.service.arxiv.ArxivService
import io.gumichan01.gakusci.domain.service.gutendex.GutendexService
import io.gumichan01.gakusci.domain.service.hal.HalService
import io.gumichan01.gakusci.domain.service.jikan.JikanAnimeService
import io.gumichan01.gakusci.domain.service.jikan.JikanMangaService
import io.gumichan01.gakusci.domain.service.openlib.OpenLibraryBookService
import io.gumichan01.gakusci.domain.service.openlib.OpenLibrarySearchService
import io.gumichan01.gakusci.domain.service.theses.ThesesService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher,
                       private val cache: SearchAggregatorCache = SearchAggregatorCacheBuilder().build()) {

    private val logger: Logger = LoggerFactory.getLogger(SearchAggregator::class.java)
    private val searchResultConsumer = SearchResultConsumer()

    suspend fun retrieveResults(queryParam: QueryParam): SearchResponse {
        val start: Int = queryParam.start
        val query: String = queryParam.query
        val (total: Int, entries: List<IResultEntry>) = cache.coget(query) {
            searchResultConsumer.consume(searchLauncher.launch(queryParam))
        }
        return entries.slice(IntRange(start, start + queryParam.rows - 1)).let { finalEntries ->
            SearchResponse(total, start, finalEntries).also { response ->
                logger.trace("$query - Total: ${response.totalResults}, number of entries: ${response.entries.size}")
            }
        }
        /*return SearchResponse(total, start, entries).slice(start, queryParam.rows).also { response ->
            logger.trace("${queryParam.query} - Total: ${response.totalResults}, number of entries: ${response.entries.size}")
        }*/
    }


    // Depending on the type of the search domain (research papers, books), a dedicated aggregator must be built
    class Builder(private var services: MutableSet<IService> = mutableSetOf()) {

        fun withResearchServices(): Builder = apply { services.addAll(DomainSearchType.RESEARCH) }
        fun withBookServices(): Builder = apply { services.addAll(DomainSearchType.BOOKS) }
        fun withMangaServices(): Builder = apply { services.addAll(DomainSearchType.MANGAS) }
        fun withAnimeServices(): Builder = apply { services.addAll((DomainSearchType.ANIME)) }

        fun build(): SearchAggregator {
            return SearchAggregator(SearchLauncher(services))
        }
    }

    private object DomainSearchType {
        val RESEARCH: Set<IService> by lazy {
            setOf(HalService(HalClient()), ArxivService(ArxivClient()), ThesesService(ThesesClient()))
        }
        val BOOKS: Set<IService> by lazy {
            setOf(OpenLibrarySearchService(OpenLibrarySearchClient()), OpenLibraryBookService(OpenLibraryBookClient()),
                JikanMangaService(JikanMangaClient()), GutendexService(GutendexClient()))
        }
        val MANGAS: Set<IService> by lazy { setOf(JikanMangaService(JikanMangaClient())) }
        val ANIME: Set<IService> by lazy { setOf(JikanAnimeService(JikanAnimeClient())) }
    }
}
