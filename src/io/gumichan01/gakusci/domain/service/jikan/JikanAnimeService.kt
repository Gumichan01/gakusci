package io.gumichan01.gakusci.domain.service.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.jikan.JikanAnimeResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.AnimeEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache
import io.gumichan01.gakusci.domain.utils.defaultThumbnailLink

class JikanAnimeService(private val jikanClient: IClient<JikanAnimeResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return cache.coget(query.query) {
            jikanClient.retrieveResults(query)?.let { response ->
                val results: List<AnimeEntry> = response.entries.map { entry ->
                    AnimeEntry(entry.title, entry.episodes, entry.url, entry.imageUrl ?: defaultThumbnailLink())
                }
                ServiceResponse(results.size, results)
            } ?: ServiceResponse(0, emptyList())
        }
    }
}