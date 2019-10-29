package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull

@ExperimentalCoroutinesApi
class SearchAggregator(private val searchLauncher: SearchLauncher) {

    suspend fun retrieveResultsFromQuery(query: String): List<ResultEntry> {
        val channel = searchLauncher.launch(query)
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
}