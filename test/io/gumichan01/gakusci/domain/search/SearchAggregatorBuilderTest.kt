package io.gumichan01.gakusci.domain.search

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class SearchAggregatorBuilderTest {

    @Test
    fun `build research aggregator linked to no service`() {
        assertThat(SearchAggregatorBuilder.build(SearchType.RESEARCH)).isInstanceOf(SearchAggregator::class.java)

    }
}