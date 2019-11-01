package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SearchLauncher(private val services: Set<IService>) {

    private val logger: Logger = LoggerFactory.getLogger(SearchLauncher::class.java)

    fun launch(query: String): Channel<ResultEntry> {
        if (services.isEmpty()) {
            return Channel<ResultEntry>(0).run { close(); this }
        }

        val serviceCallTimeout = 15000L
        val runningCoroutinesCounter: AtomicInt = atomic(services.size)
        val channel = Channel<ResultEntry>(capacity = 64)
        services.forEach { service ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        service.search(query).forEach { result -> channel.send(result) }
                    }
                } catch (e: Exception) {
                    logger.warn(e.message)
                    e.message?.let { message -> cancel(message, e) }
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