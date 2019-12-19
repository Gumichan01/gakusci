package io.gumichan01.gakusci.domain.utils

import com.github.benmanes.caffeine.cache.Cache
import io.gumichan01.gakusci.domain.model.ServiceResponse

fun Cache<Pair<String, Int>, ServiceResponse>.getOrUpdateCache(
    key: Pair<String, Int>, f: () -> ServiceResponse
): ServiceResponse {
    val keyByQueryName: Pair<String, Int>? = getKeyOrNullByQueryName(key.query())
    if (keyByQueryName != null && keyByQueryName.rows() < key.rows()) {
        invalidate(keyByQueryName)
    }
    return getCachedValue(key, f)
}

private fun Cache<Pair<String, Int>, ServiceResponse>.getCachedValue(
    key: Pair<String, Int>, f: () -> ServiceResponse
): ServiceResponse {
    return get(key) { f() } ?: throw IllegalStateException("Illegal state of cache: ${asMap()}")
}

private fun Cache<Pair<String, Int>, ServiceResponse>.getKeyOrNullByQueryName(query: String): Pair<String, Int>? {
    return asMap().keys.asSequence().filter { key -> key != null }.filter { key -> key.query() == query }.take(1)
        .firstOrNull()
}

private fun Pair<String, Int>.query() = first
private fun Pair<String, Int>.rows() = second