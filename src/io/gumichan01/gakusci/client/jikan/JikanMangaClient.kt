package io.gumichan01.gakusci.client.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import moe.ganen.jikankt.JikanKt
import moe.ganen.jikankt.models.search.MangaSearchQuery

class JikanMangaClient : IClient<JikanMangaResponse> {
    override suspend fun retrieveResults(queryParam: QueryParam): JikanMangaResponse? {
        return JikanKt.searchManga(queryParam.query, MangaSearchQuery(limit = queryParam.rows)).results
            ?.let {
                val entries: List<JikanMangaEntry> = it.asSequence()
                    .filterNotNull()
                    .filter { e -> e.title != null }
                    .filter { e -> e.url != null }
                    .filter { e -> e.startDate != null }
                    .map { entry ->
                        JikanMangaEntry(
                            entry.title!!,
                            entry.url!!,
                            DateInterval(entry.startDate!!, entry.endDate),
                            entry.imageUrl
                        )
                    }.toList()
                JikanMangaResponse(entries)
            }
    }
}