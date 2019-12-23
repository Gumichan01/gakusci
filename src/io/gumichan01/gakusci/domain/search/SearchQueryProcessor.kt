package io.gumichan01.gakusci.domain.search

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SearchQueryProcessor(
    private val cache: Cache<Pair<String, Int>, ServiceResponse> =
        Caffeine.newBuilder().maximumSize(10L).build<Pair<String, Int>, ServiceResponse>()
) {
    // TODO CACHE 1. Create a cache class that extends Cache<Pair<String, Int>, ServiceResponse>
    // TODO CACHE 2. Create a cache handler that handles several caches (one caching system per search type)

    suspend fun proceed(queryParam: QueryParam): SearchResponse {
        return SearchAggregator.Builder().run {
            when (queryParam.searchType) {
                SearchType.RESEARCH, SearchType.RESEARCHES -> withResearchServices().withCache(cache)
                SearchType.BOOKS -> this.withCache(cache) // TODO handle books
            }
        }.build().retrieveResults(queryParam)
    }
}