package io.gumichan01.gakusci.client.arxiv

import com.ouattararomuald.syndication.Syndication
import io.github.bucket4j.Bucket
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.arxiv.internal.ArxivAtomReader
import io.gumichan01.gakusci.client.arxiv.internal.ArxivUtils
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed
import io.gumichan01.gakusci.client.utils.NUM_ENTRIES_PER_SERVICE
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.SimpleQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class ArxivClient(private val cache: ArxivCache = ArxivCache()) : IClient<ArxivResponse> {

    private val logger: Logger = LoggerFactory.getLogger(ArxivClient::class.java)
    private val arxivUrl = "http://export.arxiv.org/api/query?search_query=all:%s&id_list=&start=0&max_results=%d"
    private val rateLimiter: LocalBucket = createLimiter()

    private fun createLimiter(): LocalBucket {
        return Bucket.builder()
            .addLimit { limit -> limit.capacity(1).refillGreedy(1L, 3.seconds.toJavaDuration()) }
            .build()
    }

    override suspend fun retrieveResults(query: SimpleQuery): ArxivResponse {
        return if (rateLimiter.tryConsume(1L)) {
            try {
                val url: String = arxivUrl.format(URLEncoder.encode(query.query, Charsets.UTF_8), NUM_ENTRIES_PER_SERVICE)
                cache.get(url) { endpoint ->
                    val arxivFeed: ArxivFeed = Syndication(endpoint).create(ArxivAtomReader::class.java).readAtom()
                    ArxivResponse(arxivFeed.totalResults, arxivFeed.results())
                }
            } catch (e: Exception) {
                trace(logger, e)
                ArxivResponse(0, emptyList())
            }
        } else ArxivResponse(0, emptyList())
    }

    private fun ArxivFeed.results(): List<ArxivResultEntry> {
        return entries?.map { e ->
            ArxivResultEntry(
                e.authors.map { a -> ArxivAuthor(a.name) },
                e.title,
                ArxivUtils.toDate(e.published),
                ArxivUtils.getWebsiteLink(e.links).href
            )
        } ?: emptyList()
    }
}
