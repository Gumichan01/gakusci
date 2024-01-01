package io.gumichan01.gakusci.client.jikan

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.NUM_ENTRIES_PER_SERVICE
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.utils.DateInterval
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pw.mihou.jaikan.Jaikan
import pw.mihou.jaikan.endpoints.Endpoints
import pw.mihou.jaikan.models.Manga
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class JikanMangaClient : IClient<JikanMangaResponse> {

    private val rateLimiter: LocalBucket by lazy {
        Bucket.builder()
                .addLimit(Bandwidth.classic(2, Refill.greedy(1L, 500.milliseconds.toJavaDuration())))
                .build()
    }

    override suspend fun retrieveResults(query: SimpleQuery): JikanMangaResponse? {
        val requestTimeout: Duration = 5.seconds
        return if (rateLimiter.tryConsume(1L)) {
            val entries: List<JikanMangaEntry> = withContext(Dispatchers.IO) {
                Jaikan.list(Endpoints.SEARCH, Manga::class.java, "manga", URLEncoder.encode(query.query, Charsets.UTF_8))
                        .thenApply { mangaList ->
                            mangaList.asSequence()
                                    .filterNotNull()
                                    .filter { e -> e.title != null }
                                    .filter { e -> e.url != null }
                                    .filter { e -> e.published.from != null }
                                    .map { entry ->
                                        JikanMangaEntry(
                                                entry.title!!,
                                                entry.url!!,
                                                DateInterval(entry.published.from!!, entry.published.to),
                                                entry.images.firstDefault()
                                        )
                                    }.take(NUM_ENTRIES_PER_SERVICE).toList()
                        }.get(requestTimeout.inWholeSeconds, TimeUnit.SECONDS)
            }
            JikanMangaResponse(entries)
        } else null
    }
}
