package io.gumichan01.gakusci.domain.search.cache

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class ResultCacheTest {

    @Test
    fun `Create cache and check if it is empty`() {
        val cache = ResultCache()
        assertThat(cache.isEmpty()).isTrue()
    }

    @Test
    fun `Create cache, put new entry and check if the cache is not empty`() {
        val cache = ResultCache()
        cache.put("lorem", ServiceResponse(1, listOf(ResultEntry("", ""))))
        assertThat(cache.isEmpty()).isFalse()
    }

    @Test
    fun `Create cache, put new entry and get it`() {
        val cache = ResultCache()
        val response = ServiceResponse(1, listOf(ResultEntry("", "")))
        cache.put("lorem", response)
        assertThat(cache.get("lorem", response)).isEqualTo(response)
    }

    @Test
    fun `Create cache and get a non-existing entry, get cached value instead`() {
        val cache = ResultCache()
        val response = ServiceResponse(1, listOf(ResultEntry("", "")))
        assertThat(cache.get("lorem", response)).isEqualTo(response)
    }

    @Test
    fun `Create cache, put several entries and get the value associated to "lorem"`() {
        val cache = ResultCache()
        val response = ServiceResponse(1, listOf(ResultEntry("", "")))
        cache.put("lorem", response)
        cache.put("ipsum", ServiceResponse(1, listOf(ResultEntry("a", "b"))))
        cache.put("gaku", ServiceResponse(1, listOf(ResultEntry("sci", ""))))
        assertThat(cache.get("lorem", response)).isEqualTo(response)
    }
}