package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Cache
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SearchCache(cache: Cache<String, ServiceResponse?>) :
    Cache<String, ServiceResponse?> by cache {

    private val mutex = Mutex()

    suspend fun getOrUpdateCache(param: QueryParam, f: suspend () -> ServiceResponse?): ServiceResponse? {
        mutex.withLock {
            val response: ServiceResponse? = getIfPresent(param.query)
            return if (response != null) {
                if (response.entries.size < param.rows) {
                    invalidate(param.query)
                    f()?.also { put(param.query, it) }
                } else {
                    response
                }
            } else f()?.also { put(param.query, it) }
        }
    }
}
