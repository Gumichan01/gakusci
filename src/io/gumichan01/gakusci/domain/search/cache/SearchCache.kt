package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Cache
import io.gumichan01.gakusci.domain.model.ServiceResponse

class SearchCache(cache: Cache<Pair<String, Int>, ServiceResponse>) :
    Cache<Pair<String, Int>, ServiceResponse> by cache {

    fun getOrUpdateCache(key: Pair<String, Int>, f: () -> ServiceResponse): ServiceResponse {
        val keyByQueryName: Pair<String, Int>? = getKeyOrNullByQueryName(key.query())
        return if (keyByQueryName != null) {
            if (keyByQueryName.rows() < key.rows()) {
                invalidate(keyByQueryName)
                getCachedValue(key, f)
            } else {
                getCachedValue(keyByQueryName, f)
            }
        } else getCachedValue(key, f)
    }

    private fun getCachedValue(key: Pair<String, Int>, f: () -> ServiceResponse): ServiceResponse {
        return get(key) { f() } ?: throw IllegalStateException("Illegal state of cache: ${asMap()}")
    }

    private fun getKeyOrNullByQueryName(query: String): Pair<String, Int>? {
        return asMap().keys.asSequence()
            .filter { key -> key != null }
            .filter { key -> key.query() == query }.take(1)
            .firstOrNull()
    }

    private fun Pair<String, Int>.query() = first
    private fun Pair<String, Int>.rows() = second
}
