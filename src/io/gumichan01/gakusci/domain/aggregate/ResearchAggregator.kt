package io.gumichan01.gakusci.domain.aggregate

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.receiveOrNull

@ExperimentalCoroutinesApi
class ResearchAggregator(private val services: Set<IService>) {

    suspend fun search(query: String): List<ResultEntry> {
        return consumeResults(processRequest(query))
    }

    private suspend fun consumeResults(channels: List<ReceiveChannel<ResultEntry>>): MutableList<ResultEntry> {
        val results: MutableList<ResultEntry> = mutableListOf()
        while (channels.any { channel -> !channel.isClosedForReceive }) {
            channels.forEach { channel ->
                val resultEntry: ResultEntry? = channel.receiveOrNull()
                resultEntry?.let { entry -> results += entry }
            }
        }
        return results
    }

    private fun processRequest(query: String): List<ReceiveChannel<ResultEntry>> {
        return services.map { service ->
            CoroutineScope(Dispatchers.Default).produce(capacity = 64) {
                service.search(query).forEach { result -> send(result) }
                close()
            }
        }
    }
}