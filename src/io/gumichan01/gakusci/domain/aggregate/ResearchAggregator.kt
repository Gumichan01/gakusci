package io.gumichan01.gakusci.domain.aggregate

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull
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

    private suspend fun consumeResults(channel: Channel<ResultEntry>): MutableList<ResultEntry> {
        val results: MutableList<ResultEntry> = mutableListOf()
        while (!channel.isClosedForReceive) {
            val resultEntry: ResultEntry? = channel.receiveOrNull()
            resultEntry?.let { entry -> results += entry }
        }
        return results
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