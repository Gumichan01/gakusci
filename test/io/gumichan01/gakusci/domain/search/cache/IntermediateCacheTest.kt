package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class IntermediateCacheTest {

    private val builder = Caffeine.newBuilder().maximumSize(8L)
    private val expectedResponse1: List<String> = emptyList()
    private val expectedResponse2 = listOf("hello", "world")
    private val f1 = suspend { expectedResponse1 }
    private val f2 = suspend { expectedResponse2 }

    @Test
    fun `Get intermetiate new cache value - must returns the calculated value`() {
        val cache = IntermediateCache(builder.build())
        val result = runBlocking { cache.getOrUpdateCache("lorem", f1) }
        Assertions.assertThat(result).isEqualTo(expectedResponse1)
    }

    @Test
    fun `Get intermetiate cache value - make the same query twice - must returns the cached value`() {
        val cache = IntermediateCache(builder.build())
        val result = runBlocking {
            cache.getOrUpdateCache("lorem", f2)
            cache.getOrUpdateCache("lorem", f1)
        }
        Assertions.assertThat(result).isEqualTo(expectedResponse2)
    }
}