package io.gumichan01.gakusci.domain.search

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.getOrUpdateCache
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

    fun retrieveResults(queryParam: QueryParam): SearchResponse {
        val start: Int = queryParam.start
        val (total, entries) = cacheImpl.getOrUpdateCache(Pair(queryParam.query, queryParam.rows)) {
            consume(queryParam)
        }
        logger.trace("Estimated cache size: ${cacheImpl.estimatedSize()}")
        logger.trace("Total results: $total, start: $start, number of entries: ${entries.size}")
        return SearchResponse(total, start, entries).take(queryParam.rows)
            .slice(start, queryParam.numPerPage)
    }

    private fun consume(queryParam: QueryParam): ServiceResponse =
        CoroutineScope(Dispatchers.Default).future { searchResultConsumer.consume(searchLauncher.launch(queryParam)) }.get()
}
