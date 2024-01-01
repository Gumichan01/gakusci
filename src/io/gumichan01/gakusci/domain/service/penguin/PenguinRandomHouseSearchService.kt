package io.gumichan01.gakusci.domain.service.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class PenguinRandomHouseSearchService(
    private val searchClient: IClient<PenguinRandomHouseSearchResponse>,
    private val searchService: IService
) : IService {

    private val nbMaxEntries = 10
    private val runningCoroutinesCounter: AtomicInt = atomic(nbMaxEntries)

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return (searchClient.retrieveResults(query)?.entries?.retrieveDistinctIsbns()
            ?: emptyList()).map { entry ->
            SimpleQuery(entry)
        }.let { isbnsQueries ->
            when {
                isbnsQueries.isEmpty() -> ServiceResponse(0, emptyList())
                isbnsQueries.size == 1 -> searchService.search(isbnsQueries.first())
                else -> retrieveResultsFromExternalService(isbnsQueries)
            }
        }
    }

    private fun List<Pair<String, Set<String>>>.retrieveDistinctIsbns(): List<String> {
        return map { (_: String, isbns: Set<String>) -> isbns }.distinct()
            .asSequence()
            .filter { s -> s.isNotEmpty() }
            .map { isbns -> isbns.first() }
            // In order avoid to many parallel calls to the ISBN service, take only 10 entries
            .take(nbMaxEntries).toList()
    }

    private suspend fun retrieveResultsFromExternalService(isbnEntries: List<SimpleQuery>): ServiceResponse {
        return launchRequests(isbnEntries)
            .consumeAsFlow().filterNotNull().map { response -> response.entries }
            .fold(mutableListOf<IResultEntry>()) { currentList, entries -> currentList.addAll(entries); currentList }
            .toList().let { entries -> ServiceResponse(entries.size, entries) }
    }

    private suspend fun launchRequests(queries: List<SimpleQuery>): Channel<ServiceResponse> {
        val serviceCallTimeout = 10000L
        val channel: Channel<ServiceResponse> = Channel(nbMaxEntries)
        queries.forEach { query ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        searchService.search(query).let { response -> channel.send(response) }
                    }
                } finally {
                    if (runningCoroutinesCounter.decrementAndGet() == 0) {
                        channel.close()
                    }
                }
            }
        }
        return channel
    }
}
