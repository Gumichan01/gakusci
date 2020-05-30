package io.gumichan01.gakusci.client.jikan

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import moe.ganen.jikankt.JikanKt
import moe.ganen.jikankt.models.search.MangaSearchQuery
import java.time.Duration

class JikanMangaClient : IClient<JikanMangaResponse> {

    private val rateLimiter: LocalBucket by lazy {
        Bucket4j.builder()
            .addLimit(Bandwidth.classic(2, Refill.greedy(1L, Duration.ofMillis(500))))
            .build()
    }

    override suspend fun retrieveResults(queryParam: QueryParam): JikanMangaResponse? {
        return if (rateLimiter.tryConsume(1L)) {
            JikanKt.searchManga(queryParam.query, MangaSearchQuery(limit = queryParam.rows)).results
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
        } else null
    }
}