package io.gumichan01.gakusci.domain.search


import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
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
class SearchAggregator(private val searchLauncher: SearchLauncher) {

    private val logger: Logger = LoggerFactory.getLogger(SearchAggregator::class.java)

    private val searchResultConsumer = SearchResultConsumer()
    private val cacheImpl: Cache<Pair<String, Int>, ServiceResponse> =
        Caffeine.newBuilder().maximumSize(10L).build<Pair<String, Int>, ServiceResponse>()

    // TODO Update the cache if the number of results requested is bigger
    fun retrieveResults(queryParam: QueryParam): SearchResponse {
        val start: Int = queryParam.start
        val (total, entries) = cacheImpl.getOrUpdateCache(Pair(queryParam.query, queryParam.rows)) { consume(queryParam) }
        logger.trace("Estimated cache size: ${cacheImpl.estimatedSize()}")
        logger.trace("Total results: $total, start: $start, number of entries: ${entries.size}")
        return SearchResponse(total, start, entries).take(queryParam.rows)
            .slice(IntRange(start, start + queryParam.numPerPage - 1))
    }

    private fun consume(queryParam: QueryParam): ServiceResponse =
        CoroutineScope(Dispatchers.Default).future { searchResultConsumer.consume(searchLauncher.launch(queryParam)) }.get()

    private fun Cache<Pair<String, Int>, ServiceResponse>.getOrUpdateCache(
        key: Pair<String, Int>, f: () -> ServiceResponse
    ): ServiceResponse {
        val keyByQueryName: Pair<String, Int>? = getKeyOrNullByQueryName(key.query())
        if (keyByQueryName != null && keyByQueryName.rows() < key.rows()) {
            invalidate(keyByQueryName)
        }
        return getCachedValue(key, f)
    }

    private fun Cache<Pair<String, Int>, ServiceResponse>.getCachedValue(
        key: Pair<String, Int>, f: () -> ServiceResponse
    ): ServiceResponse {
        return get(key) { f() } ?: throw IllegalStateException("Illegal state of cache: ${cacheImpl.asMap()}")
    }
}

private fun Cache<Pair<String, Int>, ServiceResponse>.getKeyOrNullByQueryName(query: String): Pair<String, Int>? {
    return asMap().keys.asSequence().filter { key -> key != null }.filter { key -> key.query() == query }.take(1)
        .firstOrNull()
}

private fun Pair<String, Int>.query() = first
private fun Pair<String, Int>.rows() = second
