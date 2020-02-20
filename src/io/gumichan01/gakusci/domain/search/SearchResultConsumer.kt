package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map

/*
    The SearchResultConsumer is the consumer, as in the Producer/Consumer pattern.
    It retrieves results provided by external service in order to process the data aggregation.
*/
@FlowPreview
@ExperimentalCoroutinesApi
class SearchResultConsumer {
    suspend fun consume(channel: Channel<ServiceResponse>): ServiceResponse {
        return if (channel.isClosedForReceive) {
            ServiceResponse(0, emptyList())
        } else {
            consumeResults(channel)
        }
    }

    private suspend fun consumeResults(channel: Channel<ServiceResponse>): ServiceResponse {
        val (total, results) = channel.consumeAsFlow()
            .map { (total, responseEntries) -> Pair(total, listOf(responseEntries)) }
            .fold(Pair<Int, List<List<IResultEntry>>>(0, emptyList())) { p1, p2 ->
                Pair(p1.first + p2.first, p1.second + p2.second)
            }
        return produceResponse(total, results)
    }

    /*
     Each result is a list of entries sent by one service
     Since I don't want the user to get results grouped by services, I shuffle them.

     Example (with 2 services):

     - Service A returns [ a, b, c ]
     - Service B returns [ d, e, f ]

     Instead of returning [ a, b, c, d, e, f ], the consumer returns [ a, d, b, e, c, f ]
     */
    private fun produceResponse(total: Int, results: List<List<IResultEntry>>): ServiceResponse {
        val entries: MutableList<IResultEntry> = mutableListOf()
        var sequences = results
        while (sequences.isNotEmpty()) {
            sequences = sequences.filter { seq -> seq.isNotEmpty() }
                .onEach { seq -> entries.add(seq.first()) }
                .map { seq -> seq.drop(1) }
        }
        return ServiceResponse(total, entries)
    }
}