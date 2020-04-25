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

    fun launch(queryParam: QueryParam): Channel<ServiceResponse> {
        if (services.isEmpty()) {
            return Channel<ServiceResponse>(0).run { close(); this }
        }

        val serviceCallTimeout = 20000L
        val nbServices = services.size
        val runningCoroutinesCounter: AtomicInt = atomic(nbServices)
        val channel = Channel<ServiceResponse>(capacity = 8)

        // In order to avoid service overloading, the aggregator balances the load between them
        val optimizedQueryParam = optimizeQuery(queryParam, nbServices)

        // Each coroutine a service is launched in is a producer of search results, as in the Producer/Consumer pattern
        services.forEach { service ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    withTimeoutOrNull(serviceCallTimeout) {
                        service.search(optimizedQueryParam)?.let { channel.send(it) }
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

    private fun optimizeQuery(queryParam: QueryParam, nbServices: Int): QueryParam {
        val maxLoad = 99
        return if (queryParam.rows >= maxLoad)
            queryParam.copy(rows = queryParam.rows / nbServices)
        else queryParam
    }
}