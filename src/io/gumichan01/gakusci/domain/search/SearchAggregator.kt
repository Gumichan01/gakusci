package io.gumichan01.gakusci.domain.search


import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.future.future
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher) {

    private val logger: Logger = LoggerFactory.getLogger(SearchAggregator::class.java)

    private val searchResultConsumer = SearchResultConsumer()
    private val cacheImpl: Cache<String, ServiceResponse> =
        Caffeine.newBuilder().maximumSize(10L).build<String, ServiceResponse>()

    // TODO Select n first results
    // TODO Set pagination
    fun retrieveResults(queryParam: QueryParam): SearchResponse {
        val (total, entries) = cacheImpl.getCachedValue(queryParam.query) { consume(queryParam) }
        logger.trace("Estimated cache size: ${cacheImpl.estimatedSize()}")
        logger.trace("Total results: $total, start: ${queryParam.start}, number of entries: ${entries.size}")
        return SearchResponse(total, 0, entries)
    }

    private fun consume(queryParam: QueryParam): ServiceResponse =
        CoroutineScope(Dispatchers.Default).future { searchResultConsumer.consume(searchLauncher.launch(queryParam)) }.get()

    private fun Cache<String, ServiceResponse>.getCachedValue(key: String, f: () -> ServiceResponse): ServiceResponse {
        return get(key) { f() } ?: throw IllegalStateException("Illegal state of cache: ${cacheImpl.asMap()}")
    }
}
