package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.None
import io.gumichan01.gakusci.utils.Option
import io.gumichan01.gakusci.utils.Some
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull

@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher) {
    
    // TODO Select n first results
    // TODO Set pagination
    suspend fun retrieveResults(query: String): ServiceResponse {
        val channel = searchLauncher.launch(query)
        return consumeResults(channel)
    }

    // TODO Check if this implementation is memory-consuming
    private suspend fun consumeResults(channel: Channel<Option<ServiceResponse>>): ServiceResponse {
        return consumeResultsAux(channel, ServiceResponse(0, 0, emptyList()))
    }

    private tailrec suspend fun consumeResultsAux(
        chan: Channel<Option<ServiceResponse>>,
        acc: ServiceResponse
    ): ServiceResponse {
        if (chan.isClosedForReceive) {
            return acc
        }
        val acc2 = when (val response: Option<ServiceResponse> = chan.receiveOrNull() ?: None) {
            is Some -> acc.copy(
                totalResults = acc.totalResults + response.t.totalResults,
                entries = acc.entries + response.t.entries
            )
            is None -> acc
        }
        return consumeResultsAux(chan, acc2)
    }
}