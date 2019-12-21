package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HalService(private val halClient: HalClient) : IService {
    private val logger: Logger = LoggerFactory.getLogger(HalService::class.java)

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return try {
            val (totalResults, _, results) = halClient.retrieveResults(queryParam).body
            val entries: List<ResultEntry> =
                results?.map { e -> ResultEntry(e.label, e.uri) } ?: emptyList()
            ServiceResponse(totalResults, entries)
        } catch (e: Exception) {
            logger.trace(e.message)
            null
        }
    }
}