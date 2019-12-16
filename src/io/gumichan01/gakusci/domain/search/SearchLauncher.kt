package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.utils.Option
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class SearchLauncher(private val services: Set<IService>) {

    fun launch(queryParam: QueryParam): Channel<Option<ServiceResponse>> {
        if (services.isEmpty()) {
            return Channel<Option<ServiceResponse>>(0).run { close(); this }
        }

        val serviceCallTimeout = 15000L
        val runningCoroutinesCounter: AtomicInt = atomic(services.size)
        val channel = Channel<Option<ServiceResponse>>(capacity = 64)
        services.forEach { service ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        channel.send(service.search(queryParam))
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