package io.gumichan01.gakusci.domain.search.cache

import io.gumichan01.gakusci.domain.model.ServiceResponse

class ResultCache {

    private var cacheImpl: MutableMap<String, ServiceResponse> = mutableMapOf()

    fun isEmpty(): Boolean {
        return cacheImpl.isEmpty()
    }

    fun put(key: String, value: ServiceResponse) {
        cacheImpl.put(key, value)
    }

    fun get(key: String, defaultValue: ServiceResponse): ServiceResponse {
        return cacheImpl.getOrDefault(key, defaultValue)
    }
}