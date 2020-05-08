package io.gumichan01.gakusci.domain.search

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

class IntermediateCache(
    cache: Cache<String, List<String>> = Caffeine.newBuilder().expireAfterWrite(600, TimeUnit.SECONDS)
        .maximumSize(100L).build()
) : Cache<String, List<String>> by cache {

    private val mutex = Mutex()

    suspend fun getOrUpdateCache(key: String, f: suspend () -> List<String>): List<String> {
        mutex.withLock {
            return getIfPresent(key) ?: f().also { put(key, it) }
        }
    }
}
