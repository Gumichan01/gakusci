package io.gumichan01.gakusci.domain.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.ServiceResponse
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit

class ServiceRequestCache(cache: Cache<String, ServiceResponse> = defaultCache()) :
    Cache<String, ServiceResponse> by cache {

    /**
     * This is basically the same thing as get(), but for suspended functions
     */
    suspend fun coget(query: String, f: suspend () -> ServiceResponse?): ServiceResponse? {
        val hashmap: ConcurrentMap<String, ServiceResponse> = asMap()
        return if (hashmap.containsKey(query) && hashmap[query] != null) {
            hashmap[query]!!
        } else {
            val value: ServiceResponse? = f()
            if (value != null) {
                hashmap[query] = value
            }
            value
        }
    }

    companion object {
        fun defaultCache(): Cache<String, ServiceResponse> {
            return Caffeine.newBuilder().expireAfterWrite(1200, TimeUnit.SECONDS).maximumSize(200L).build()
        }
    }
}