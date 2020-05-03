package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.SearchType
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

    private val cache: SearchCache = CacheHandler().createFreshCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.getOrUpdateCache(queryParam) {
            searchClient.retrieveResults(queryParam)?.let { response ->
                response.isbnEntries.distinct().map { isbn -> QueryParam(isbn, SearchType.BOOKS) }.toList().run {
                    when {
                        isEmpty() -> ServiceResponse(0, emptyList())
                        size == 1 -> searchService.search(first())
                        else -> retrieveResultsFromExternalService(this)
                    }
                }
            }
        }
    }

    private suspend fun retrieveResultsFromExternalService(isbnEntries: List<QueryParam>): ServiceResponse? {
        return launchRequests(isbnEntries)
            .consumeAsFlow().filterNotNull().map { response -> response.entries }
            .fold(mutableListOf<IResultEntry>()) { currentList, entries -> currentList.addAll(entries); currentList }
            .toList().let { entries -> ServiceResponse(entries.size, entries) }
    }

    private suspend fun launchRequests(queries: List<QueryParam>): Channel<ServiceResponse?> {
        val serviceCallTimeout = 15000L
        val channel = Channel<ServiceResponse?>(queries.size)
        val runningCoroutinesCounter: AtomicInt = atomic(queries.size)
        queries.forEach { query ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        searchService.search(query)?.let { response -> channel.send(response) }
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