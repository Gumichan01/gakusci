package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Cache
import io.gumichan01.gakusci.domain.model.ServiceResponse
import java.util.concurrent.ConcurrentMap

class SearchCache(cache: Cache<String, ServiceResponse>) :
    Cache<String, ServiceResponse> by cache {

    suspend fun coget(query: String, f: suspend () -> ServiceResponse): ServiceResponse {
        val hashmap: ConcurrentMap<String, ServiceResponse> = asMap()
        return if (hashmap.containsKey(query) && hashmap[query] != null) {
            hashmap[query]!!
        } else {
            val value: ServiceResponse = f()
            hashmap[query] = value
            value
        }
    }
}
