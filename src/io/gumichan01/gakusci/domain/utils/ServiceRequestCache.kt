package io.gumichan01.gakusci.domain.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import java.util.concurrent.TimeUnit

class ServiceRequestCache(cache: Cache<String, ServiceResponse>) :
    Cache<String, ServiceResponse> by cache {
    companion object {
        fun defaultCache(): Cache<String, ArxivResponse> {
            return Caffeine.newBuilder().expireAfterWrite(1200, TimeUnit.SECONDS).maximumSize(200L).build()
        }
    }
}