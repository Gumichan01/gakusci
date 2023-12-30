package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

class SearchAggregatorCacheBuilder {

    private val builder: Caffeine<Any, Any> by lazy {
        Caffeine.newBuilder().expireAfterWrite(600, TimeUnit.SECONDS).maximumSize(100L)
    }

    fun generateAggregatorCache(): SearchAggregatorCache {
        return SearchAggregatorCache(builder.build())
    }
}
