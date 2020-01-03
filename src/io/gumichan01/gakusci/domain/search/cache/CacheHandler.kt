package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import java.util.concurrent.TimeUnit

class CacheHandler {

    private val builder: Caffeine<Any, Any> by lazy {
        Caffeine.newBuilder().expireAfterWrite(600, TimeUnit.SECONDS).maximumSize(8L)
    }
    private val researchCache: SearchCache by lazy { SearchCache(builder.build<String, ServiceResponse>()) }
    private val bookCache: SearchCache by lazy { SearchCache(builder.build<String, ServiceResponse>()) }

    fun provideCache(searchType: SearchType): SearchCache {
        return when (searchType) {
            SearchType.RESEARCH, SearchType.RESEARCHES -> researchCache
            SearchType.BOOKS -> bookCache
        }
    }
}
