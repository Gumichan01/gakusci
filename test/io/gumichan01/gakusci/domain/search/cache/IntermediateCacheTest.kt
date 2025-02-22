package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.jspecify.annotations.NonNull
import kotlin.test.Test

class IntermediateCacheTest {

    private val builder: @NonNull Caffeine<Any, Any> = Caffeine.newBuilder().maximumSize(8L)
    private val expectedResponse1: List<Pair<String, Set<String>>> = emptyList()
    private val expectedResponse2: List<Pair<String, Set<String>>> = listOf(
        Pair("hello", emptySet()), Pair("world", emptySet())
    )
    private val f1: suspend () -> List<Pair<String, Set<String>>> = suspend { expectedResponse1 }
    private val f2: suspend () -> List<Pair<String, Set<String>>> = suspend { expectedResponse2 }

    @Test
    fun `Get intermediate new cache value - must returns the calculated value`() {
        val cache = IntermediateCache<List<Pair<String, Set<String>>>>(builder.build())
        val result = runBlocking { cache.getOrUpdateCache("lorem", f1) }
        Assertions.assertThat(result).isEqualTo(expectedResponse1)
    }

    @Test
    fun `Get intermediate cache value - make the same query twice - must returns the cached value`() {
        val cache = IntermediateCache<List<Pair<String, Set<String>>>>(builder.build())
        val result = runBlocking {
            cache.getOrUpdateCache("lorem", f2)
            cache.getOrUpdateCache("lorem", f1)
        }
        Assertions.assertThat(result).isEqualTo(expectedResponse2)
    }
}