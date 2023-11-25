package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class SearchLauncher(private val services: Set<IService>) {

    private val runningCoroutinesCounter: AtomicInt = atomic(services.size)

    fun launch(queryParam: QueryParam): Channel<ServiceResponse> {
        if (services.isEmpty()) {
            return Channel<ServiceResponse>(0).run { close(); this }
        }

        val serviceCallTimeout = 20000L
        val channel: Channel<ServiceResponse> = Channel(capacity = 8)

        // Each coroutine a service is launched in is a producer of search results, as in the Producer/Consumer pattern
        services.forEach { service ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        service.search(queryParam)?.let { channel.send(it) }
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
