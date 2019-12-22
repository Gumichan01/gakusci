package io.gumichan01.gakusci.client.arxiv

import com.ouattararomuald.syndication.Syndication
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.arxiv.internal.ArxivAtomReader
import io.gumichan01.gakusci.client.arxiv.internal.ArxivUtils
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed
import io.gumichan01.gakusci.domain.model.QueryParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

class ArxivClient : IClient<ArxivResponse> {

    private val logger: Logger = LoggerFactory.getLogger(ArxivClient::class.java)
    private val arxivUrl = "https://export.arxiv.org/api/query?search_query=%s&max_results=%d"
    private val rateLimiter: LocalBucket = createLimiter()

    private fun createLimiter(): LocalBucket {
        return Bucket4j.builder()
            .addLimit(Bandwidth.classic(1, Refill.greedy(1L, Duration.ofSeconds(3))))
            .build()
    }

    override suspend fun retrieveResults(queryParam: QueryParam): ArxivResponse? {
        return if (rateLimiter.tryConsume(1L)) {
            try {
                val url: String = arxivUrl.format(queryParam.query, queryParam.rows)
                val arxivFeed: ArxivFeed = Syndication(url).create(ArxivAtomReader::class.java).readAtom()
                ArxivResponse(arxivFeed.totalResults, arxivFeed.results())
            } catch (e: Exception) {
                logger.trace(e.message)
                if (logger.isTraceEnabled) {
                    e.printStackTrace()
                }
                null
            }
        } else null
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