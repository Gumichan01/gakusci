package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull

@ExperimentalCoroutinesApi
class SearchAggregator(private val services: Set<IService>) {

    suspend fun search(query: String): List<ResultEntry> {
        if (services.isEmpty()) {
            return emptyList()
        }

        val channel = Channel<ResultEntry>(capacity = 64)
        launchRequest(query, channel)
        return consumeResults(channel)
    }

    private suspend fun consumeResults(channel: Channel<ResultEntry>): List<ResultEntry> {
        return consumeResultsAux(channel, emptyList())
    }

    private tailrec suspend fun consumeResultsAux(
        chan: Channel<ResultEntry>,
        acc: List<ResultEntry>
    ): List<ResultEntry> {
        if (chan.isClosedForReceive) {
            return acc
        }
        val resultEntry: ResultEntry? = chan.receiveOrNull()
        val nacc = if (resultEntry != null) acc + resultEntry else acc
        return consumeResultsAux(chan, nacc)
    }

    private fun launchRequest(query: String, channel: Channel<ResultEntry>) {
        val serviceCallTimeout = 15000L
        val runningCoroutinesCounter: AtomicInt = atomic(services.size)
        services.forEach { service ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        service.search(query).forEach { result -> channel.send(result) }
                    }
                } finally {
                    if (runningCoroutinesCounter.decrementAndGet() == 0) {
                        channel.close()
                    }
                }
            }
        }
    }
}