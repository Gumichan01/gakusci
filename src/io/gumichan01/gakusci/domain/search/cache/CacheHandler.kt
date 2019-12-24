package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType

class CacheHandler {
    fun provideCache(searchType: SearchType): SearchCache {
        return SearchCache(Caffeine.newBuilder().maximumSize(10L).build<Pair<String, Int>, ServiceResponse>())
    }
}
