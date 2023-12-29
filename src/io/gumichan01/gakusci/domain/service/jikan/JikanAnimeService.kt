package io.gumichan01.gakusci.domain.service.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.jikan.JikanAnimeResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.AnimeEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache

class JikanAnimeService(val jikanClient: IClient<JikanAnimeResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.coget(queryParam.query) {
            jikanClient.retrieveResults(queryParam)?.let { response ->
                val results: List<AnimeEntry> = response.entries.map { entry ->
                    AnimeEntry(entry.title, entry.episodes, entry.url, entry.imageUrl ?: "")
                }
                ServiceResponse(results.size, results)
            }
        }
    }
}