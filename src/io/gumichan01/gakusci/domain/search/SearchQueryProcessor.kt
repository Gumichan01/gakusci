package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/*
    This is the entry point of the search domain used by the controller in order to process the search.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class SearchQueryProcessor {
    suspend fun proceed(queryParam: QueryParam): SearchResponse {
        return SearchAggregator.Builder().run {
            when (queryParam.searchType) {
                SearchType.RESEARCH, SearchType.RESEARCHES -> withResearchServices()
                SearchType.BOOKS -> withBookServices()
                SearchType.MANGAS -> withMangaServices()
                SearchType.ANIME -> withAnimeServices()
                SearchType.MUSIC -> withMusicServices()
            }
        }.build().retrieveResults(queryParam)
    }
}