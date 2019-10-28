package io.gumichan01.gakusci.domain.aggregate

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ResearchAggregator(private val services: Set<IService>) {

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
        val resultEntry: ResultEntry = chan.receive()
        return consumeResultsAux(chan, acc + resultEntry)
    }

    private fun launchRequest(query: String, channel: Channel<ResultEntry>) {
        val runningCoroutinesCounter: AtomicInt = atomic(services.size)
        services.forEach { service ->
            CoroutineScope(Dispatchers.Default).launch {
                service.search(query).forEach { result -> channel.send(result) }
                if (runningCoroutinesCounter.decrementAndGet() == 0) {
                    channel.close()
                }
            }
        }
    }
}