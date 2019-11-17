package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.None
import io.gumichan01.gakusci.utils.Option
import io.gumichan01.gakusci.utils.Some
import org.slf4j.LoggerFactory

class HalService(private val halClient: HalClient) : IService {
    private val logger = LoggerFactory.getLogger(HalService::class.java)

    override suspend fun search(query: String): Option<ServiceResponse> {
        return try {
            val (totalResults, startIndex, results) = halClient.retrieveResults(query).body
            val entries: List<ResultEntry> =
                results?.map { e -> ResultEntry(e.label, e.uri) } ?: emptyList()
            Some(ServiceResponse(totalResults, startIndex, entries))
        } catch (e: Exception) {
            logger.trace(e.message)
            None
        }
    }
}