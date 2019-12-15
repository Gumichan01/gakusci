package io.gumichan01.gakusci.domain.search


import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.search.cache.ResultCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher) {

    private val searchResultConsumer = SearchResultConsumer()
    private val cache = ResultCache()

    // TODO Select n first results
    // TODO Set pagination
    // TODO Set cache system
    suspend fun retrieveResults(query: String): SearchResponse {
        val (total, entries) = getOrPutCachedValue(query)
        return SearchResponse(total, 0, entries)
    }

    suspend fun getOrPutCachedValue(query: String): ServiceResponse {
        val response: ServiceResponse? = cache.get(query)
        return if (response == null) {
            cache.put(query, searchResultConsumer.consume(searchLauncher.launch(query)))
            cache.get(query) ?: throw IllegalStateException("Invalid state of the cache")
        } else {
            response
        }

    }
}