package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class ArxivService(private val arxivClient: IClient<ArxivResponse>) : IService {

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return arxivClient.retrieveResults(queryParam)?.let {
            val (totalResults, results) = it
            val entries = results.map { r ->
                SimpleResultEntry(
                    r.label(),
                    r.link
                )
            }
            ServiceResponse(totalResults, entries)
        }
    }
}