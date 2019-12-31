package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Cache
import io.gumichan01.gakusci.domain.model.ServiceResponse

class SearchCache(cache: Cache<Pair<String, Int>, ServiceResponse>) :
    Cache<Pair<String, Int>, ServiceResponse> by cache {

    // TODO it is possible to invalidate the key twice. FIXME
    suspend fun getOrUpdateCache(key: Pair<String, Int>, f: suspend () -> ServiceResponse): ServiceResponse {
        val keyByQueryName: Pair<String, Int>? = getKeyOrNullByQueryName(key.query())
        return if (keyByQueryName != null) {
            if (keyByQueryName.rows() < key.rows()) {
                invalidate(keyByQueryName)
                f().also { put(key, it) }
            } else {
                getIfPresent(keyByQueryName)!!
            }
        } else f().also { put(key, it) }
    }

    // TODO asMap() is thread-safe / coroutine-safe for reading, but if the cache is modified  after the iterator is generated, the behavior is undefined, FIXME
    private fun getKeyOrNullByQueryName(query: String): Pair<String, Int>? {
        return asMap().keys.asSequence()
            .filter { key -> key != null }
            .filter { key -> key.query() == query }.take(1)
            .firstOrNull()
    }

    private fun Pair<String, Int>.query() = first
    private fun Pair<String, Int>.rows() = second
}
