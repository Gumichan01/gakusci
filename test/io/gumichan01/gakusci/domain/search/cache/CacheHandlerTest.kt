package io.gumichan01.gakusci.domain.search.cache

import io.gumichan01.gakusci.domain.utils.SearchType
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class CacheHandlerTest {

    @Test
    fun `create cache handler and retrieve research cache`() {
        assertThat(CacheHandler().provideCache(SearchType.RESEARCH)).isInstanceOf(SearchCache::class.java)
    }
}