package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

class IntermediateCache<V>(
    cache: Cache<String, V> = Caffeine.newBuilder()
        .expireAfterWrite(600, TimeUnit.SECONDS).maximumSize(100L).build()
) : Cache<String, V> by cache {

    private val mutex = Mutex()

    suspend fun getOrUpdateCache(
        key: String, f: suspend () -> V
    ): V {
        mutex.withLock {
            return getIfPresent(key) ?: f().also { put(key, it) }
        }
    }
}
