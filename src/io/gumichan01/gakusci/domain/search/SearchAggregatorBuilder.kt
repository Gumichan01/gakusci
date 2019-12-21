package io.gumichan01.gakusci.domain.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
object SearchAggregatorBuilder {
    fun build(type: SearchType): SearchAggregator {
        return SearchAggregator(SearchLauncher(type.services))
    }
}