package io.gumichan01.gakusci.domain.service.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.jikan.JikanAnimeResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.AnimeEntry
import io.gumichan01.gakusci.domain.service.IService

class JikanAnimeService(val jikanClient: IClient<JikanAnimeResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return jikanClient.retrieveResults(queryParam)?.let { response ->
            val results = response.entries.map { entry ->
                AnimeEntry(entry.title, entry.episodes, entry.url, entry.imageUrl ?: "")
            }
            ServiceResponse(results.size, results)
        }
    }
}