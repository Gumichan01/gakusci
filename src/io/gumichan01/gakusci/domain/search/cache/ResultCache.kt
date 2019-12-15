package io.gumichan01.gakusci.domain.search.cache

import io.gumichan01.gakusci.domain.model.ServiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// TODO replace this class with Caffeine LoadingCache
class ResultCache {

    private val logger: Logger = LoggerFactory.getLogger(ResultCache::class.java)

    private var cacheImpl: MutableMap<String, ServiceResponse> = mutableMapOf()

    fun isEmpty(): Boolean {
        return cacheImpl.isEmpty()
    }

    fun put(key: String, value: ServiceResponse) {
        cacheImpl.put(key, value)
    }

    fun get(key: String): ServiceResponse? {
        logger.trace("Cache size: ${cacheImpl.size}")
        return cacheImpl.get(key)
    }

    fun get(key: String, defaultValue: ServiceResponse): ServiceResponse {
        logger.trace("Cache : $key - size: ${cacheImpl.size}")
        return cacheImpl.getOrPut(key) { defaultValue }
    }
}