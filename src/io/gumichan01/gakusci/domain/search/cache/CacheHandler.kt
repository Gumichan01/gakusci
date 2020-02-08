package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.ServiceResponse
import java.util.concurrent.TimeUnit

class CacheHandler {

    private val builder: Caffeine<Any, Any> by lazy {
        Caffeine.newBuilder().expireAfterWrite(600, TimeUnit.SECONDS).maximumSize(8L)
    }

    fun createFreshCache(): SearchCache {
        return SearchCache(builder.build<String, ServiceResponse>())
    }
}
