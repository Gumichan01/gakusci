package io.gumichan01.gakusci.domain.search.cache

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class CacheHandlerTest {

    @Test
    fun `create fresh cache`() {
        assertThat(CacheHandler().createFreshCache()).isInstanceOf(SearchCache::class.java)
    }
}