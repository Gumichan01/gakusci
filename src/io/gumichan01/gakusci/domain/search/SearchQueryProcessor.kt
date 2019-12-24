package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SearchQueryProcessor(private val cacheHandler: CacheHandler = CacheHandler()) {
    fun proceed(queryParam: QueryParam): SearchResponse {
        return SearchAggregator.Builder().run {
            when (queryParam.searchType) {
                SearchType.RESEARCH, SearchType.RESEARCHES -> withResearchServices()
                SearchType.BOOKS -> TODO("handle books")
            }
        }.withCache(cacheHandler.provideCache(queryParam.searchType)).build().retrieveResults(queryParam)
    }
}