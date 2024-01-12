package io.gumichan01.gakusci.client.jikan

import io.github.bucket4j.Bucket
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.NUM_ENTRIES_PER_SERVICE
import io.gumichan01.gakusci.domain.model.SimpleQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pw.mihou.jaikan.Jaikan
import pw.mihou.jaikan.endpoints.Endpoints
import pw.mihou.jaikan.models.Anime
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class JikanAnimeClient : IClient<JikanAnimeResponse> {

    private val rateLimiter: LocalBucket by lazy {
        Bucket.builder()
            .addLimit { limit -> limit.capacity(2).refillGreedy(1L, 500.milliseconds.toJavaDuration()) }
            .build()
    }

    // TODO Rebuild the implementation of the Jikan client
    // The Jaikan implementation of the Jikan API does not properly support pagination
    override suspend fun retrieveResults(query: SimpleQuery): JikanAnimeResponse? {
        val requestTimeout: Duration = 5.seconds
        return if (rateLimiter.tryConsume(1L)) {
            val entries: List<JikanAnimeEntry> = withContext(Dispatchers.IO) {
                Jaikan.list(Endpoints.SEARCH, Anime::class.java, "anime", URLEncoder.encode(query.query, Charsets.UTF_8))
                    .thenApply { animeList ->
                        animeList.asSequence()
                            .filterNotNull()
                            .filter { e -> e.title != null }
                            .filter { e -> e.url != null }
                            .map { entry ->
                                JikanAnimeEntry(entry.title!!, entry.url!!, entry.episodes, entry.images.firstDefault())
                            }.take(NUM_ENTRIES_PER_SERVICE).toList()
                    }.get(requestTimeout.inWholeSeconds, TimeUnit.SECONDS)
            }
            JikanAnimeResponse(entries)
        } else null
    }
}
