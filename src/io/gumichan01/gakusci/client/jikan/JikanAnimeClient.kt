package io.gumichan01.gakusci.client.jikan

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import moe.ganen.jikankt.JikanKt
import moe.ganen.jikankt.models.search.AnimeSearchQuery
import java.time.Duration

class JikanAnimeClient : IClient<JikanAnimeResponse> {

    private val rateLimiter: LocalBucket by lazy {
        Bucket4j.builder()
            .addLimit(Bandwidth.classic(2, Refill.greedy(1L, Duration.ofMillis(500))))
            .build()
    }

    override suspend fun retrieveResults(queryParam: QueryParam): JikanAnimeResponse? {
        return if (rateLimiter.tryConsume(1L)) {
            JikanKt.searchAnime(queryParam.query, AnimeSearchQuery(limit = queryParam.rows)).results
                ?.let {
                    val entries: List<JikanAnimeEntry> = it.asSequence()
                        .filterNotNull()
                        .filter { e -> e.title != null }
                        .filter { e -> e.url != null }
                        .map { entry ->
                            JikanAnimeEntry(entry.title!!, entry.url!!, entry.episodes, entry.imageUrl)
                        }
                        .toList()
                    JikanAnimeResponse(entries)
                }
        } else null
    }
}