package io.gumichan01.gakusci.domain.search.cache

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class SearchAggregatorCacheBuilderTest {

    @Test
    fun `create fresh cache`() {
        assertThat(SearchAggregatorCacheBuilder().generateAggregatorCache()).isInstanceOf(SearchAggregatorCache::class.java)
    }
}