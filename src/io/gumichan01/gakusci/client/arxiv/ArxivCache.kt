package io.gumichan01.gakusci.client.arxiv

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

class ArxivCache(cache: Cache<String, ArxivResponse> = defaultArxivCache()) :
        Cache<String, ArxivResponse> by cache {

    companion object {
        fun defaultArxivCache(): Cache<String, ArxivResponse> {
            return Caffeine.newBuilder().expireAfterWrite(600, TimeUnit.SECONDS).maximumSize(100L).build()
        }
    }
}
