package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.Option
import io.gumichan01.gakusci.utils.Some
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce

@FlowPreview
@ExperimentalCoroutinesApi
class SearchResultConsumer {
    suspend fun consume(channel: Channel<Option<ServiceResponse>>): ServiceResponse {
        return if (channel.isClosedForReceive) {
            ServiceResponse(0, emptyList())
        } else {
            consumeResults(channel)
        }
    }

    private suspend fun consumeResults(channel: Channel<Option<ServiceResponse>>): ServiceResponse {
        val (total, results) = channel.consumeAsFlow()
            .filterIsInstance<Some<ServiceResponse>>()
            .map { s -> s.t }
            .map { (total, responseEntries) -> Pair(total, listOf(responseEntries)) }
            .reduce { acc, pair -> Pair(acc.first + pair.first, acc.second + pair.second) }
        return produceResponse(total, results)
        // NOTE I'm not sure if I need the start value
    }

    private fun produceResponse(total: Int, results: List<List<ResultEntry>>): ServiceResponse {
        val entries: MutableList<ResultEntry> = mutableListOf()
        var sequences = results
        while (sequences.isNotEmpty()) {
            sequences = sequences.filter { seq -> seq.isNotEmpty() }
                .onEach { seq -> entries.add(seq.first()) }
                .map { seq -> seq.drop(1) }
        }
        return ServiceResponse(total, entries)
    }
}