package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ServiceResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher) {

    private val searchResultConsumer = SearchResultConsumer()

    // TODO Select n first results
    // TODO Set pagination
    // TODO Set cache system
    suspend fun retrieveResults(query: String): ServiceResponse {
        return searchResultConsumer.consume(searchLauncher.launch(query))
    }
}