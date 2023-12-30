package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class SearchAggregatorCacheTest {

    private val builder = Caffeine.newBuilder().maximumSize(8L)
    private val expectedResponse1 = ServiceResponse(1, listOf(SimpleResultEntry("l1", "url1")))
    private val expectedResponse2 = ServiceResponse(1, listOf(SimpleResultEntry("label-test", "url-test")))
    private val f1 = suspend { expectedResponse1 }
    private val f2 = suspend { expectedResponse2 }

    @Test
    fun `Get or update cache - new value - must returns the calculated value`() {
        val cache = SearchAggregatorCache(builder.build())
        val result: ServiceResponse = runBlocking { cache.coget("lorem", f1) }
        Assertions.assertThat(result).isEqualTo(expectedResponse1)
    }

    @Test
    fun `Get or update cache - make the same query twice - must returns the cached value`() {
        val cache = SearchAggregatorCache(builder.build())
        val result: ServiceResponse = runBlocking {
            cache.coget("lorem", f1)
            cache.coget("lorem", f1)
        }
        Assertions.assertThat(result).isEqualTo(expectedResponse1)
    }

    @Test
    fun `Get or update cache - more entries requested - return the calculated value`() {
        val cache = SearchAggregatorCache(builder.build())
        val result: ServiceResponse = runBlocking {
            cache.coget("lorem", f2)
            cache.coget("lorem", f1)
        }
        Assertions.assertThat(result).isEqualTo(expectedResponse2)
    }
}