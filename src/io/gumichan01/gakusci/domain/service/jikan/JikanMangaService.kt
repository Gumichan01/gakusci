package io.gumichan01.gakusci.domain.service.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.jikan.JikanMangaResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.MangaEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache

class JikanMangaService(private val jikanClient: IClient<JikanMangaResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.coget(queryParam.query) {
            jikanClient.retrieveResults(queryParam)?.let { response ->
                val results: List<MangaEntry> = response.entries.map { entry ->
                    MangaEntry(entry.title, entry.publicationPeriod, entry.url, entry.imageUrl ?: "")
                }
                ServiceResponse(results.size, results)
            }
        }
    }
}